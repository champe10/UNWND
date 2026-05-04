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
import java.util.Locale

@Composable
fun FavoritesScreen(
    viewModel: UnwndViewModel,
    onPlaceClick: (String) -> Unit
) {
    val favorites by viewModel.filteredPlaces.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val favoritePlaces = favorites.filter { it.isFavorite }

    FavoritesContent(
        favoritePlaces = favoritePlaces,
        userLocation = userLocation,
        calculateDistance = viewModel::calculateDistance,
        onToggleFavorite = viewModel::toggleFavorite,
        onPlaceClick = onPlaceClick
    )
}

@Composable
fun FavoritesContent(
    favoritePlaces: List<com.example.unwnd.data.model.Place>,
    userLocation: Pair<Double, Double>?,
    calculateDistance: (Double, Double, Double, Double) -> Float,
    onToggleFavorite: (String) -> Unit,
    onPlaceClick: (String) -> Unit
) {
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
                    val distanceText = userLocation?.let { (lat, long) ->
                        val distance = calculateDistance(lat, long, place.latitude, place.longitude)
                        String.format(Locale.getDefault(), "%.1f km", distance)
                    }
                    PlaceCard(
                        place = place,
                        distanceText = distanceText,
                        onToggleFavorite = { onToggleFavorite(place.id) },
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
        FavoritesContent(
            favoritePlaces = listOf(
                com.example.unwnd.data.model.Place(
                    id = "1",
                    name = "The Belverdere Grill",
                    description = "Sample description",
                    imageUrl = "",
                    location = "Nairobi",
                    rating = 4.5,
                    reviewsCount = 100,
                    category = "Rooftop",
                    latitude = -1.3,
                    longitude = 36.8,
                    priceRange = "$$$",
                    isFavorite = true,
                    tags = listOf("Chill")
                )
            ),
            userLocation = Pair(-1.274, 36.781),
            calculateDistance = { _, _, _, _ -> 5.2f },
            onToggleFavorite = {},
            onPlaceClick = {}
        )
    }
}
