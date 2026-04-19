package com.example.unwnd.data.model

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val rating: Double,
    val reviewsCount: Int,
    val category: String,
    val priceRange: String,
    val distance: String,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val openingHours: List<OpeningHour> = emptyList(),
    val waitTime: String? = null
)

data class OpeningHour(
    val day: String,
    val hours: String,
    val isToday: Boolean = false
)
