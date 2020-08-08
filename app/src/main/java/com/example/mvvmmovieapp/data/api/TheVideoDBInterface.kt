package com.example.mvvmmovieapp.data.api

import com.example.mvvmmovieapp.data.vo.Video
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TheVideoDBInterface {

    @GET("watch")
    fun getTrailer(@Query("v") key: String): Single<Video>
}