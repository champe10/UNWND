package com.example.unwnd.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.unwnd.ui.viewmodel.UnwndViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ExploreScreen(
    viewModel: UnwndViewModel
) {

    val places by viewModel.filteredPlaces.collectAsState()

    val nairobi = LatLng(-1.2921, 36.8219)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nairobi, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        places.forEach { place ->

            Marker(
                state = MarkerState(
                    position = LatLng(
                        place.latitude,
                        place.longitude
                    )
                ),
                title = place.name,
                snippet = place.location
            )
        }
    }
        Text(
            text = "Places: ${places.size}",
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

