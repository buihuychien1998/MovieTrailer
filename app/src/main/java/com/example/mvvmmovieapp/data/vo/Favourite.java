package com.example.mvvmmovieapp.data.vo;

public class Favourite {
    private String movieId;
    private String movieName;
    private String userId;


    public Favourite(String movieId, String movieName, String userId) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
