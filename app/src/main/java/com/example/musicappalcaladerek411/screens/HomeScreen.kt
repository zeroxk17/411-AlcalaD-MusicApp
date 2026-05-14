package com.example.musicappalcaladerek411.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicappalcaladerek411.components.AlbumCarouselCard
import com.example.musicappalcaladerek411.components.AlbumListItem
import com.example.musicappalcaladerek411.components.MiniPlayer
import com.example.musicappalcaladerek411.models.Album
import com.example.musicappalcaladerek411.navigation.AlbumDetail
import com.example.musicappalcaladerek411.services.MusicService
import com.example.musicappalcaladerek411.ui.theme.AppBackground
import com.example.musicappalcaladerek411.ui.theme.HeaderGradientEnd
import com.example.musicappalcaladerek411.ui.theme.HeaderGradientStart
import com.example.musicappalcaladerek411.ui.theme.MusicAppAlcalaDerek411Theme
import com.example.musicappalcaladerek411.ui.theme.PrimaryPurple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://musicapi.pjasoft.com/api/"

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(),
    navController: NavController = rememberNavController()
) {
    var albums by remember { mutableStateOf(listOf<Album>()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val result = async(Dispatchers.IO) {
                retrofit.create(MusicService::class.java).getAllAlbums()
            }
            albums = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("HomeScreen", e.message.toString())
            hasError = true
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(innerPadding)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            hasError -> {
                Text(
                    text = "Error al cargar álbumes. Verifica tu conexión.",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    item {
                        HomeHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        SectionHeader(title = "Albums", actionText = "See more")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(albums) { album ->
                                AlbumCarouselCard(
                                    album = album,
                                    onClick = {
                                        navController.navigate(AlbumDetail(albumId = album.id))
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        SectionHeader(title = "Recently Played", actionText = "See more")
                    }
                    items(albums) { album ->
                        AlbumListItem(
                            album = album,
                            onClick = {
                                navController.navigate(AlbumDetail(albumId = album.id))
                            }
                        )
                    }
                }

                if (albums.isNotEmpty()) {
                    MiniPlayer(
                        album = albums.first(),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(HeaderGradientStart, HeaderGradientEnd)
                )
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Good Morning!",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Derek Alcala",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = PrimaryPurple,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    MusicAppAlcalaDerek411Theme {
        HomeScreen()
    }
}
