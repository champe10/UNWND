package com.example.unwnd.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
        val LOCATION_PERMISSION_GRANTED = booleanPreferencesKey("location_permission_granted")
        
        // Notification Settings
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
        val TRENDING_ALERTS = booleanPreferencesKey("trending_alerts")

        // App Settings
        val DISTANCE_UNIT = stringPreferencesKey("distance_unit")
    }

    val selectedCategory: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_CATEGORY] ?: "All"
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETED] ?: false
    }

    val notificationSettings: Flow<NotificationSettings> = context.dataStore.data.map { prefs ->
        NotificationSettings(
            pushEnabled = prefs[PUSH_NOTIFICATIONS] ?: true,
            trendingAlerts = prefs[TRENDING_ALERTS] ?: true
        )
    }

    val appSettings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            darkMode = prefs[DARK_MODE_ENABLED] ?: false,
            distanceUnit = prefs[DISTANCE_UNIT] ?: "km"
        )
    }

    suspend fun saveSelectedCategory(category: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_CATEGORY] = category
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLocationPermissionGranted(granted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[LOCATION_PERMISSION_GRANTED] = granted
        }
    }

    suspend fun updateNotificationSettings(settings: NotificationSettings) {
        context.dataStore.edit { prefs ->
            prefs[PUSH_NOTIFICATIONS] = settings.pushEnabled
            prefs[TRENDING_ALERTS] = settings.trendingAlerts
        }
    }

    suspend fun updateAppSettings(settings: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_ENABLED] = settings.darkMode
            prefs[DISTANCE_UNIT] = settings.distanceUnit
        }
    }
    
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}

data class NotificationSettings(
    val pushEnabled: Boolean,
    val trendingAlerts: Boolean
)

data class AppSettings(
    val darkMode: Boolean,
    val distanceUnit: String
)
