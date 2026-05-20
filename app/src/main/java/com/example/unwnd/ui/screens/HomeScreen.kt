package com.example.unwnd.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unwnd.data.model.Place
import com.example.unwnd.ui.components.CategoryChip
import com.example.unwnd.ui.components.UnwndSearchBar
import com.example.unwnd.ui.theme.PrimaryYellow
import com.example.unwnd.ui.theme.SecondaryOrange
import com.example.unwnd.ui.viewmodel.UnwndViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.example.unwnd.ui.theme.UNWNDTheme
import com.example.unwnd.utils.getUserLocation
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: UnwndViewModel,
    onPlaceClick: (String) -> Unit
) {
    val userLocation by viewModel.userLocation.collectAsState()
    val context = LocalContext.current
    val hasLocationPermission = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission.value = isGranted
    }

    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value) {
            getUserLocation(context) { lat, long ->
                viewModel.updateUserLocation(lat, long)
            }
        }
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val places by viewModel.filteredPlaces.collectAsState()




    val trendingPlaces = places
        .sortedByDescending { it.rating * it.reviewsCount }
        .take(5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔹 Header
        item {
            Column {
                Text(
                    text = "Discover Hangouts",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Find the best spots in Nairobi",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        // Search Bar
        item {
            UnwndSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange
            )
        }

        // Categories
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.categories) { category ->
                    CategoryChip(
                        text = category,
                        isSelected = selectedCategory == category,
                        onClick = { viewModel.onCategorySelected(category) }
                    )
                }
            }
        }

        // Trending Title
        item {
            Text(
                text = "🔥 Trending Spots",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Trending Horizontal Scroll
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(items = trendingPlaces,
                    key = { it.id }
                ) { place ->
                    TrendingCard(
                        place = place,
                        onToggleFavorite = { viewModel.toggleFavorite(place.id) },
                        onClick = { onPlaceClick(place.id) }
                    )
                }
            }
        }

        // All Places Title
        item {
            Text(
                text = "All Places",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Vertical Places List
        items(
            items = places,
            key = { it.id }
        ) { place ->

            val distanceText = userLocation?.let { (lat, long) ->
                val distance = viewModel.calculateDistance(lat,
                    long,
                    place.latitude,
                    place.longitude
                )
                String.format(Locale.getDefault(), "%.1f km", distance)

            }
            PlaceCard(
                place = place,
                distanceText = distanceText,
                onToggleFavorite = { viewModel.toggleFavorite(place.id) },
                onClick = { onPlaceClick(place.id) }
            )
        }
    }
}

@Composable
fun PlaceCard(
    place: Place,
    distanceText: String? ,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = place.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                if(distanceText != null) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.BottomStart),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.6f)

                    ) {
                        Text(
                            text = distanceText,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

            }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (place.isFavorite) SecondaryOrange else Color.White
                    )
                }
            }




            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = place.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = PrimaryYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = place.rating.toString(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.location,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    place.tags.take(3).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingCard(
    place: Place,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable{
                 onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            // 🔥 Overlay text (premium feel)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = place.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = PrimaryYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.rating.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    UNWNDTheme {
        // Since HomeScreen depends on ViewModel which has complex dependencies,
        // we'd ideally refactor HomeScreen to take state/lambdas instead of ViewModel.
    }
}
