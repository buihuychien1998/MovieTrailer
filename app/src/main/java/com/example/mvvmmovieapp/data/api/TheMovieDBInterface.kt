package com.example.mvvmmovieapp.data.api

import com.example.mvvmmovieapp.data.vo.CommentResponse
import com.example.mvvmmovieapp.data.vo.MovieDetails
import com.example.mvvmmovieapp.data.vo.MovieResponse
import com.example.mvvmmovieapp.data.vo.MovieTrailer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    // https://api.themoviedb.org/3/movie/popular?api_key=6e63c2317fbe963d76c3bdc2b785f6d1&page=1
    // https://api.themoviedb.org/3/movie/299534?api_key=6e63c2317fbe963d76c3bdc2b785f6d1
    // https://api.themoviedb.org/3/

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(@Path("movie_id") id: Int): Single<MovieTrailer>

    @GET("movie/{movie_id}/reviews")
    fun getComment(@Path("movie_id") id: Int, @Query("page") page: Int): Single<CommentResponse>

    @GET("search/movie")
    fun search(@Query("query") query: String, @Query("page") page: Int): Single<MovieResponse>

}