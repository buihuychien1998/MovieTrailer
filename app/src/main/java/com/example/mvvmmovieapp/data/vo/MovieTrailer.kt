package com.example.mvvmmovieapp.data.vo

import com.google.gson.annotations.SerializedName

data class MovieTrailer(
    val id: Int,
    @SerializedName("results")
    val results: List<Trailer>
)