package com.example.unwnd.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unwnd.data.model.Place
import com.example.unwnd.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class UnwndViewModel(private val repository: PlaceRepository = PlaceRepository()) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("All", "Rooftop", "Food", "Chill", "Nightlife")

    val filteredPlaces: StateFlow<List<Place>> = combine(
        repository.places,
        _searchQuery,
        _selectedCategory
    ) { places, query, category ->
        places.filter { place ->
            val matchesQuery = place.name.contains(query, ignoreCase = true) ||
                    place.description.contains(query, ignoreCase = true) ||
                    place.tags.any { it.contains(query, ignoreCase = true) }
            val matchesCategory = category == "All" || place.category == category
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(id: String) {
        repository.toggleFavorite(id)
    }

    fun getPlaceById(id: String): StateFlow<Place?> {
        return repository.getPlaceById(id).stateIn(viewModelScope, SharingStarted.Lazily, null)
    }
}
