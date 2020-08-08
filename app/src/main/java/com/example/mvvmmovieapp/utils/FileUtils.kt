package com.example.mvvmmovieapp.utils

import android.content.Context
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.mvvmmovieapp.data.vo.Video


object FileUtils {
    fun getAllVideoFromDevice(context: Context): ArrayList<Video> {
        val videos = ArrayList<Video>()


        val musicResolver = context.getContentResolver()
        val uri = MediaStore.Files.getContentUri("external")
        //        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.TITLE,
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.ARTIST
        )
        //Lọc theo định dạng file
        val selection = MediaStore.Video.VideoColumns.MIME_TYPE + "=?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4")
        val selectionArgs = arrayOf(mimeType)
        //query Phương thức nhận yêu cầu từ Client kết quả trả về là một cursor
        val c = musicResolver.query(uri, projection, selection, selectionArgs, null)
        if (c != null) {
            while (c.moveToNext()) {
                // Create a com.hidero.musicapp.model object.
                val path = c.getString(0)   // Retrieve path.
                val title = c.getString(1)   // Retrieve name.
                val id = c.getInt(2)  // Retrieve album name.
                val artist = c.getString(3) // Retrieve artist name.

                // Set data to the com.hidero.musicapp.model object.
                val video = Video(id, title, path)
                videos.add(video)
            }
            c.close()
        }

        // Return the list.
        return videos
    }


}