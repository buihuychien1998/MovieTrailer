package com.example.mvvmmovieapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBUser.db";
    private static final String USER = "USER";
    private static final String COL_USER_ID = "userID";
    private static final String COL_NAME = "name";
    private static final String COL_PASS = "password";
    private static final String WISH_LIST = "WISHLIST";
    private static final String COL_MOVIE_ID = "movieId";
    private static final String COL_MOVIE_NAME = "movieName";
    private String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER + "(userId text primary key,name text, password text)");
        db.execSQL("create table " + WISH_LIST + "(movieId text primary key,movieName text, userId text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        db.execSQL("DROP TABLE IF EXISTS " + WISH_LIST);
    }

    public long insertUser(String userId, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, userId);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_PASS, password);
        return db.insert(USER, null, contentValues);

    }

    public long insertWishList(String movieId, String movieName, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MOVIE_ID, movieId);
        contentValues.put(COL_MOVIE_NAME, movieName);
        contentValues.put(COL_USER_ID, userId);
        return db.insert(WISH_LIST, null, contentValues);
    }

    public boolean validUser(String userId, String password) {
        Cursor c = getUser(userId, password);
        return c.getCount() > 0;
    }


    public Cursor getUser(String userId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + USER + " where userId = '" + userId + "' AND password = '" + password + "'", null);
    }

    public Cursor getWishList(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + WISH_LIST + " where userId = '" + userId + "'", null);
    }

    public long deleteWishList(String userId, String movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = "movieId = '" + movieId + "' AND userId = '" + userId + "'";
        return db.delete(WISH_LIST, where, null);
    }
}
