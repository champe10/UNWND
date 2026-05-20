package com.example.unwnd.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Singleton profile for now
    val username: String = "John Doe",
    val email: String = "john.doe@example.com",
    val bio: String = "Adventure seeker & coffee lover",
    val location: String = "New York, USA",
    val profilePictureUri: String? = null
)
