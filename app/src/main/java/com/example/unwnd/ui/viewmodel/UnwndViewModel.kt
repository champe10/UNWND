package com.example.unwnd.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unwnd.data.local.datastore.UserPreferences
import com.example.unwnd.data.model.Place
import com.example.unwnd.data.repository.PlaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UnwndViewModel(
    private val repository: PlaceRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val selectedCategory: StateFlow<String> = userPreferences.selectedCategory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "All")

    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val userLocation: StateFlow<Pair<Double, Double>?> = _userLocation.asStateFlow()

    val categories = listOf("All", "Rooftop", "Food", "Chill", "Nightlife", "Nature")

    init {
        viewModelScope.launch {
            repository.initializeData()
        }
    }


    val filteredPlaces: StateFlow<List<Place>> = combine(
        repository.places,
        _searchQuery,
        selectedCategory,
        _userLocation
    ) { places, query, category, userLocation ->
        val filtered = places.filter { place ->
            val matchesQuery = if (query.isBlank()) true else
                place.name.contains(query, true) ||
                        place.description.contains(query, true) ||
                        place.tags.any { it.contains(query, true) }

            val matchesCategory = category == "All" || place.category == category
            matchesQuery && matchesCategory
        }

        if (userLocation != null) {
            val (lat, long) = userLocation
            filtered.sortedBy {
                calculateDistance(lat, long, it.latitude, it.longitude)
            }
        } else {
            filtered
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun calculateDistance(
        userLat: Double,
        userLong: Double,
        placeLat: Double,
        placeLong: Double
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            userLat,
            userLong,
            placeLat,
            placeLong,
            results
        )
        return results[0] / 1000f
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: String) {
        viewModelScope.launch {
            userPreferences.saveSelectedCategory(category)
        }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    fun getPlaceById(id: String): StateFlow<Place?> {
        return repository.getPlaceById(id).stateIn(viewModelScope, SharingStarted.Lazily, null)
    }
}
