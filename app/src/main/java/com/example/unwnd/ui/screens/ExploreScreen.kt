package com.example.unwnd.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unwnd.ui.theme.UNWNDTheme
import com.example.unwnd.ui.viewmodel.UnwndViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExploreScreen(
    viewModel: UnwndViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Explore",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Map Discovery coming soon!",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    UNWNDTheme {
        ExploreScreen(
            viewModel = UnwndViewModel()
        )
    }
}
