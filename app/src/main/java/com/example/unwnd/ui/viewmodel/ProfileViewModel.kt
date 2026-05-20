package com.example.unwnd.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unwnd.data.local.datastore.AppSettings
import com.example.unwnd.data.local.datastore.NotificationSettings
import com.example.unwnd.data.model.UserProfile
import com.example.unwnd.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile = UserProfile(),
    val notifications: NotificationSettings = NotificationSettings(true, true),
    val settings: AppSettings = AppSettings(false, "km"),
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showClearCacheDialog: Boolean = false,
    val message: String? = null
)

class ProfileViewModel(private val repository: UserRepository, private val context: android.content.Context) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(
                repository.userProfile.map { it ?: UserProfile() },
                repository.notificationSettings,
                repository.appSettings
            ) { profile, notifications, settings ->
                ProfileUiState(
                    profile = profile,
                    notifications = notifications,
                    settings = settings,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateProfile(profile)
            _uiState.update { it.copy(isLoading = false, message = "Profile updated successfully") }
        }
    }

    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            val currentProfile = _uiState.value.profile
            repository.updateProfile(currentProfile.copy(profilePictureUri = uri))
        }
    }

    fun updateNotifications(notifications: NotificationSettings) {
        viewModelScope.launch {
            repository.updateNotificationSettings(notifications)
        }
    }

    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            repository.updateAppSettings(settings)
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun setShowLogoutDialog(show: Boolean) {
        _uiState.update { it.copy(showLogoutDialog = show) }
    }

    fun setShowClearCacheDialog(show: Boolean) {
        _uiState.update { it.copy(showClearCacheDialog = show) }
    }

    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                context.cacheDir.deleteRecursively()
                _uiState.update { it.copy(isLoading = false, showClearCacheDialog = false, message = "Cache cleared successfully") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, message = "Failed to clear cache") }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.update { it.copy(showLogoutDialog = false) }
        }
    }
}
