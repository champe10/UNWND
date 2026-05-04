package com.example.unwnd.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unwnd.data.model.Place
import com.example.unwnd.ui.theme.PrimaryYellow
import androidx.compose.ui.tooling.preview.Preview
import com.example.unwnd.ui.theme.SecondaryOrange
import com.example.unwnd.ui.viewmodel.UnwndViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    placeId: String,
    viewModel: UnwndViewModel,
    onBackClick: () -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val placeState by viewModel.getPlaceById(placeId).collectAsState()

    placeState?.let { place ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 88.dp)
            ) {
                DetailHeader(
                    place = place,
                    onBackClick = onBackClick,
                    onToggleFavorite = { viewModel.toggleFavorite(place.id) }
                )
                DetailInfo(place = place)
            }

            // Bottom Get Directions Button
            Button(
                onClick = onNavigateToExplore,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryOrange)
            ) {
                Icon(Icons.Default.Directions, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Get Directions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DetailHeader(
    place: Place,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        AsyncImage(
            model = place.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for better icon visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        // Top Navigation and Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = onToggleFavorite,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            ) {
                Icon(
                    imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (place.isFavorite) SecondaryOrange else Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailInfo(place: Place) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = place.name,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 36.sp,
                modifier = Modifier.weight(1f)
            )
            
            Surface(
                color = PrimaryYellow.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = PrimaryYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = SecondaryOrange,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = place.location,
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tags as rounded chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            place.tags.forEach { tag ->
                Surface(
                    shape = CircleShape,
                    color = Color.LightGray.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = tag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Description",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = place.description,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = Color.Black.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val samplePlace = com.example.unwnd.data.model.Place(
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
    com.example.unwnd.ui.theme.UNWNDTheme {
        Column {
            DetailHeader(
                place = samplePlace,
                onBackClick = {},
                onToggleFavorite = {}
            )
            DetailInfo(place = samplePlace)
        }
    }
}
