package com.example.unwnd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.unwnd.data.local.datastore.UserPreferences
import com.example.unwnd.ui.navigation.UnwndNavigation
import com.example.unwnd.ui.theme.UNWNDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val userPrefs = remember { UserPreferences(context) }
            val appSettings by userPrefs.appSettings.collectAsState(initial = null)
            
            UNWNDTheme(darkTheme = appSettings?.darkMode ?: isSystemInDarkTheme()) {
                UnwndNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    UNWNDTheme {
        UnwndNavigation()
    }
}
