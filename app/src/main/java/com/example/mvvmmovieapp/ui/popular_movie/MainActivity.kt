package com.example.mvvmmovieapp.ui.popular_movie

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmmovieapp.R
import com.example.mvvmmovieapp.data.api.TheMovieDBClient
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface
import com.example.mvvmmovieapp.data.repository.NetworkState
import com.example.mvvmmovieapp.data.vo.Slide
import com.example.mvvmmovieapp.ui.account.AccountActivity
import com.example.mvvmmovieapp.ui.account.WishListActivity
import com.example.mvvmmovieapp.ui.offline.OfflineActivity
import com.example.mvvmmovieapp.ui.search.SearchActivity
import com.example.mvvmmovieapp.utils.Const.REQUEST_PERMISSION
import com.example.mvvmmovieapp.utils.Const.USERID
import com.example.mvvmmovieapp.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var sliders: ArrayList<Slide>
    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_PERMISSION

        )
        sliders = ArrayList()
        with(sliders){
            add(
                Slide(
                    "http://www.phimmoi.net/phim/tro-choi-ky-ao-2-i2-8594/",
                    "http://image.phimmoi.net/film/8594/poster.medium.jpg",
                    "Trò chơi kì ảo"
                )
            )
            add(
                Slide(
                    "http://www.phimmoi.net/phim/bay-vien-ngoc-rong-hanh-tinh-nguc-tu-i2-6896/",
                    "http://image.phimmoi.net/film/6896/poster.medium.jpg",
                    "Bảy viên ngọc rồng"
                )
            )
            add(
                Slide(
                    "https://bilutv.org/phim-moi-tinh-dau-cua-thieu-nu-tung-trai-i1-16211.html?1583295167",
                    "https://bilutv.org/upload/images/2020/01/moi-tinh-dau-cua-thieu-nu-tung-trai-2020_1580119836-slider.jpg",
                    "Mối tình đầu của thiếu nữ từng trải"
                )
            )
            add(
                Slide(
                    "https://bilutv.org/phim-tam-sinh-tam-the-cham-thuong-thu-i5-12627.html",
                    "https://bilutv.org/upload/images/2020/01/tam-sinh-tam-the-cham-thuong-thu-2018_1578551626.jpg",
                    "Tam Sinh Tam Thế: Chẩm Thượng Thư"
                )
            )
        }
        val adapter = SliderPagerAdapter(this, sliders)
        sliderPager.adapter = adapter
        // setup timer
        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
        indicator.setupWithViewPager(sliderPager, true)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this@MainActivity)

        val gridLayoutManager = GridLayoutManager(this@MainActivity, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }
        with(rcMain) {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            this.adapter = movieAdapter
        }

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.mvvmmovieapp.R.drawable.ic_list)
        navMain.setNavigationItemSelectedListener {
            when (it.itemId) {

                com.example.mvvmmovieapp.R.id.navLogin -> {
                    startActivity(Intent(this@MainActivity, AccountActivity::class.java))
                }
                com.example.mvvmmovieapp.R.id.navWishList -> {
                    startActivity(Intent(this@MainActivity, WishListActivity::class.java))
                }
                R.id.navOffline -> {
                    startActivity(Intent(this@MainActivity, OfflineActivity::class.java))
                }

                R.id.navSignOut -> {
                    SharedPrefs.instance.put(USERID, "")
                    navMain.menu.getItem(3).isEnabled = false
                }

            }
            with(navMain.menu) {
                getItem(0).isChecked = false
                getItem(1).isChecked = false
                getItem(2).isChecked = false
                getItem(3).isChecked = false
            }
            drawerLayout.closeDrawers()
            true

        }
        btnSearch.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        navMain.menu.getItem(3).isEnabled = !TextUtils.isEmpty(SharedPrefs.instance[USERID, String::class.java, ""])

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), REQUEST_PERMISSION
                        )
                    }
                    Toast.makeText(
                        this@MainActivity,
                        "Permission denied to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true

            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

    internal inner class SliderTimer : TimerTask() {
        override fun run() {

            this@MainActivity.runOnUiThread {
                if (sliderPager.currentItem < sliders.size - 1) {
                    sliderPager.currentItem = sliderPager.currentItem + 1
                } else
                    sliderPager.currentItem = 0
            }


        }
    }

}
