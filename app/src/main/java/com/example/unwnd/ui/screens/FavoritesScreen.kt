package com.example.unwnd.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    viewModel: UnwndViewModel,
    onPlaceClick: (String) -> Unit
) {
    val favorites by viewModel.filteredPlaces.collectAsState()
    val favoritePlaces = favorites.filter { it.isFavorite }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Places",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (favoritePlaces.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved places yet",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoritePlaces) { place ->
                    PlaceCard(
                        place = place,
                        onToggleFavorite = { viewModel.toggleFavorite(place.id) },
                        onClick = { onPlaceClick(place.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    UNWNDTheme {
        FavoritesScreen(
            viewModel = UnwndViewModel(),
            onPlaceClick = {}
        )
    }
}
