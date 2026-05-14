package com.example.musicappalcaladerek411.models

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName(value = "id", alternate = ["_id"])
    val id: String,
    val title: String,
    val artist: String,
    val image: String,
    val description: String
)
