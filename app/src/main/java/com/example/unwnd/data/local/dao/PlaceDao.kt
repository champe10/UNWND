package com.example.unwnd.data.local.dao

import androidx.room.*
import com.example.unwnd.data.model.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<Place>>

    @Query("SELECT * FROM places WHERE id = :id")
    fun getPlaceById(id: String): Flow<Place?>

    @Query("SELECT * FROM places WHERE isFavorite = 1")
    fun getFavoritePlaces(): Flow<List<Place>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<Place>): List<Long>

    @Update
    suspend fun updatePlace(place: Place): Int

    @Query("UPDATE places SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean): Int
}
