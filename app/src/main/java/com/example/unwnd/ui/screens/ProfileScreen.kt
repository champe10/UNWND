package com.example.unwnd.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unwnd.data.local.datastore.AppSettings
import com.example.unwnd.data.local.datastore.NotificationSettings
import com.example.unwnd.data.model.UserProfile
import com.example.unwnd.ui.theme.SecondaryOrange
import com.example.unwnd.ui.viewmodel.ProfileUiState
import com.example.unwnd.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Profile Header
                ProfileHeader(
                    profile = uiState.profile,
                    onImagePicked = { viewModel.updateProfilePicture(it) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Expandable Sections
                ExpandableSection(
                    title = "Edit Profile",
                    icon = Icons.Default.Person,
                    initialExpanded = false
                ) {
                    EditProfileSection(
                        profile = uiState.profile,
                        onSave = { viewModel.updateProfile(it) },
                        isLoading = uiState.isLoading
                    )
                }

                ExpandableSection(
                    title = "Notifications",
                    icon = Icons.Default.Notifications,
                    initialExpanded = false
                ) {
                    NotificationSection(
                        settings = uiState.notifications,
                        onChanged = { viewModel.updateNotifications(it) }
                    )
                }

                ExpandableSection(
                    title = "Settings",
                    icon = Icons.Default.Settings,
                    initialExpanded = false
                ) {
                    SettingsSection(
                        settings = uiState.settings,
                        onChanged = { viewModel.updateSettings(it) },
                        onClearCacheClick = { viewModel.setShowClearCacheDialog(true) }
                    )
                }

                ExpandableSection(
                    title = "Help & Support",
                    icon = Icons.Default.Help,
                    initialExpanded = false
                ) {
                    HelpSupportSection()
                }

                Spacer(modifier = Modifier.height(40.dp))

                LogoutButton(onLogoutClick = { viewModel.setShowLogoutDialog(true) })

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SecondaryOrange)
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.logout() },
            onDismiss = { viewModel.setShowLogoutDialog(false) }
        )
    }

    if (uiState.showClearCacheDialog) {
        ClearCacheConfirmationDialog(
            onConfirm = { viewModel.clearCache() },
            onDismiss = { viewModel.setShowClearCacheDialog(false) }
        )
    }
}

@Composable
fun ProfileHeader(profile: UserProfile, onImagePicked: (String) -> Unit) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { onImagePicked(it.toString()) }
        }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = profile.profilePictureUri ?: "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Surface(
                color = SecondaryOrange,
                shape = CircleShape,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Edit Profile Picture",
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = profile.username,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = profile.bio,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun ExpandableSection(
    title: String,
    icon: ImageVector,
    initialExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initialExpanded) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 2.dp else 0.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SecondaryOrange.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = SecondaryOrange,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun EditProfileSection(profile: UserProfile, onSave: (UserProfile) -> Unit, isLoading: Boolean) {
    var name by remember { mutableStateOf(profile.username) }
    var bio by remember { mutableStateOf(profile.bio) }
    var location by remember { mutableStateOf(profile.location) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        )
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 3,
            enabled = !isLoading
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        )
        Button(
            onClick = { 
                if (name.isNotBlank()) {
                    onSave(profile.copy(username = name, bio = bio, location = location)) 
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SecondaryOrange),
            enabled = !isLoading && name.isNotBlank()
        ) {
            Text(if (isLoading) "Saving..." else "Save Changes")
        }
    }
}

@Composable
fun NotificationSection(settings: NotificationSettings, onChanged: (NotificationSettings) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SwitchItem(label = "Push Notifications", checked = settings.pushEnabled) {
            onChanged(settings.copy(pushEnabled = it))
        }
        SwitchItem(label = "Trending Spots", checked = settings.trendingAlerts) {
            onChanged(settings.copy(trendingAlerts = it))
        }
    }
}

@Composable
fun SettingsSection(settings: AppSettings, onChanged: (AppSettings) -> Unit, onClearCacheClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", fontWeight = FontWeight.Medium)
            Switch(
                checked = settings.darkMode,
                onCheckedChange = { onChanged(settings.copy(darkMode = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = SecondaryOrange)
            )
        }
        
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
        
        Text("Distance Unit", fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("km", "mi").forEach { unit ->
                val selected = settings.distanceUnit == unit
                FilterChip(
                    selected = selected,
                    onClick = { onChanged(settings.copy(distanceUnit = unit)) },
                    label = { Text(if (unit == "km") "Kilometres (km)" else "Miles (mi)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SecondaryOrange.copy(alpha = 0.1f),
                        selectedLabelColor = SecondaryOrange
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Button(
            onClick = onClearCacheClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Red)
        ) {
            Text("Clear Cache")
        }
        
        Text(
            text = "App Version 1.0.2 (Build 42)",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun HelpSupportSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SupportItem(icon = Icons.Default.QuestionAnswer, label = "Frequently Asked Questions")
        SupportItem(icon = Icons.Default.Email, label = "Contact Support")
        SupportItem(icon = Icons.Default.BugReport, label = "Report a Bug")
        SupportItem(icon = Icons.Default.Info, label = "About Unwnd")
    }
}

@Composable
fun SwitchItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = SecondaryOrange)
        )
    }
}

@Composable
fun SupportItem(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    Button(
        onClick = onLogoutClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFF1F0),
            contentColor = Color(0xFFE53935)
        )
    ) {
        Icon(Icons.Default.Logout, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Logout") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Logout", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ClearCacheConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clear Cache") },
        text = { Text("Are you sure you want to clear the app cache? This will remove temporary images and data.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Clear", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
