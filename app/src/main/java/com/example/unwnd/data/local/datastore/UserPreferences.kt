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
    }

    val selectedCategory: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_CATEGORY] ?: "All"
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETED] ?: false
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
}
