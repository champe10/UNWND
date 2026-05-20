package com.example.unwnd.data.repository

import com.example.unwnd.data.local.dao.UserProfileDao
import com.example.unwnd.data.local.datastore.AppSettings
import com.example.unwnd.data.local.datastore.NotificationSettings
import com.example.unwnd.data.local.datastore.UserPreferences
import com.example.unwnd.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userProfileDao: UserProfileDao,
    private val userPreferences: UserPreferences
) {
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()
    val notificationSettings: Flow<NotificationSettings> = userPreferences.notificationSettings
    val appSettings: Flow<AppSettings> = userPreferences.appSettings

    suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdate(profile)
    }

    suspend fun updateNotificationSettings(settings: NotificationSettings) {
        userPreferences.updateNotificationSettings(settings)
    }

    suspend fun updateAppSettings(settings: AppSettings) {
        userPreferences.updateAppSettings(settings)
    }

    suspend fun logout() {
        userPreferences.clearAll()
        // Here you would also clear Room tables if needed, 
        // but for a singleton profile we might just reset it
        userProfileDao.insertOrUpdate(UserProfile()) 
    }
}
