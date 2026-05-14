package com.example.musicappalcaladerek411.services

import com.example.musicappalcaladerek411.models.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicService {

    @GET("albums")
    suspend fun getAllAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}
