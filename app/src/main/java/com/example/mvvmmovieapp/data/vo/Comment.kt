package com.example.mvvmmovieapp.data.vo

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String)