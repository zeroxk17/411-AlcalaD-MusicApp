package com.example.musicappalcaladerek411

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.example.musicappalcaladerek411.navigation.AlbumDetail
import com.example.musicappalcaladerek411.navigation.Home
import com.example.musicappalcaladerek411.screens.AlbumDetailScreen
import com.example.musicappalcaladerek411.screens.HomeScreen
import com.example.musicappalcaladerek411.ui.theme.MusicAppAlcalaDerek411Theme
import okhttp3.OkHttpClient

class MusicApplication : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "MusicAppAlcalaDerek411/1.0")
                        .build()
                )
            }
            .build()
        return ImageLoader.Builder(context)
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { client })) }
            .crossfade(true)
            .build()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppAlcalaDerek411Theme {
                MusicApp()
            }
        }
    }
}

@Composable
fun MusicApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home
        ) {
            composable<Home> {
                HomeScreen(
                    innerPadding = innerPadding,
                    navController = navController
                )
            }
            composable<AlbumDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<AlbumDetail>()
                AlbumDetailScreen(
                    albumId = route.albumId,
                    innerPadding = innerPadding,
                    navController = navController
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MusicAppPreview() {
    MusicAppAlcalaDerek411Theme {
        MusicApp()
    }
}
