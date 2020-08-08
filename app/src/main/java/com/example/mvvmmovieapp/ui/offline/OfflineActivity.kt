package com.example.mvvmmovieapp.ui.offline

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmmovieapp.R
import com.example.mvvmmovieapp.utils.Const.PATH
import com.example.mvvmmovieapp.utils.FileUtils
import kotlinx.android.synthetic.main.activity_offline.*

class OfflineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)
        val videos = FileUtils.getAllVideoFromDevice(this)
        val titles = ArrayList<String>()
        for (video in videos) {
            titles.add(video.title)
        }
        lvVideo.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        lvVideo.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, PlayVideoActivity::class.java)
            intent.putExtra(PATH, videos[i].path)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}
