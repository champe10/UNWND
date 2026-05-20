package com.example.unwnd.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(

    @PrimaryKey val id: String,

    val name: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewsCount: Int,
    val category: String,
    val priceRange: String,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false


)

