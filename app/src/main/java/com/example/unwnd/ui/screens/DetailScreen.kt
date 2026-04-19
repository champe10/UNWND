package com.example.unwnd.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.unwnd.ui.theme.SecondaryOrange
import com.example.unwnd.ui.viewmodel.UnwndViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.example.unwnd.ui.theme.UNWNDTheme

@Composable
fun DetailScreen(
    placeId: String,
    viewModel: UnwndViewModel,
    onBackClick: () -> Unit
) {
    val placeState by viewModel.getPlaceById(placeId).collectAsState()

    placeState?.let { currentPlace ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
                    AsyncImage(
                        model = currentPlace.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                                    startY = 150f
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(alpha = 0.7f))
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        IconButton(
                            onClick = { viewModel.toggleFavorite(currentPlace.id) },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(alpha = 0.7f))
                        ) {
                            Icon(
                                imageVector = if (currentPlace.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (currentPlace.isFavorite) SecondaryOrange else Color.Black
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentPlace.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = PrimaryYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentPlace.rating.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Text(
                        text = "${currentPlace.category} • ${currentPlace.priceRange}",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = SecondaryOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = currentPlace.location, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "About",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentPlace.description,
                        color = Color.Gray,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (currentPlace.openingHours.isNotEmpty()) {
                        Text(
                            text = "Opening Hours",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        currentPlace.openingHours.forEach { hour ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = hour.day,
                                    color = if (hour.isToday) Color.Black else Color.Gray,
                                    fontWeight = if (hour.isToday) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = hour.hours,
                                    color = if (hour.isToday) SecondaryOrange else Color.Gray,
                                    fontWeight = if (hour.isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            Button(
                onClick = { /* Implement Booking or Directions */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryOrange)
            ) {
                Text("Reserve a Table", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    UNWNDTheme {
        DetailScreen(
            placeId = "1",
            viewModel = UnwndViewModel(),
            onBackClick = {}
        )
    }
}
