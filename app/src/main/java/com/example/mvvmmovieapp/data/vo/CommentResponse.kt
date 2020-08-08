package com.example.mvvmmovieapp.data.vo

import com.google.gson.annotations.SerializedName

class CommentResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val commentList: List<Comment>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)