package com.example.mvvmmovieapp.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmmovieapp.data.vo.Slide
import kotlinx.android.synthetic.main.item_slider.view.*


class SliderPagerAdapter(private val mContext: Context, private val mList: List<Slide>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return mList.size
    }


    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(com.example.mvvmmovieapp.R.layout.item_slider, container, false)
        Glide.with(mContext).apply {
            RequestOptions().placeholder(com.example.mvvmmovieapp.R.drawable.poster_placeholder)
                .error(
                    com.example.mvvmmovieapp.R.drawable.error
                ).fitCenter()
        }.load(mList[position].image).into(slideLayout.ivSlider)
        slideLayout.tvTitle.setText(mList[position].title)
        slideLayout.fab.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mList[position].link))
            mContext.startActivity(browserIntent)
        }
        container.addView(slideLayout)
        return slideLayout


    }

    override fun isViewFromObject(@NonNull view: View, @NonNull o: Any): Boolean {
        return view === o
    }


    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as View)
    }
}
