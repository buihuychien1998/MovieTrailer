package com.example.mvvmmovieapp.ui.account

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmmovieapp.data.repository.DBHelper
import com.example.mvvmmovieapp.data.vo.Favourite
import com.example.mvvmmovieapp.ui.single_movie_details.SingleMovieActivity
import com.example.mvvmmovieapp.utils.Const.USERID
import com.example.mvvmmovieapp.utils.SharedPrefs
import com.example.mvvmmovieapp.utils.showToast
import kotlinx.android.synthetic.main.activity_wish_list.*


class WishListActivity : AppCompatActivity() {
    private lateinit var arrayList: ArrayList<Favourite>
    private lateinit var adapter: WishListAdapter
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(this)
        setContentView(com.example.mvvmmovieapp.R.layout.activity_wish_list)
        arrayList = ArrayList()
        adapter = WishListAdapter(this, com.example.mvvmmovieapp.R.layout.item_favorite, arrayList)
        if (!TextUtils.isEmpty(SharedPrefs.instance[USERID, String::class.java, ""])) {
            loadData(SharedPrefs.instance[USERID, String::class.java, ""])
            lvWishList.adapter = adapter
            lvWishList.setOnItemLongClickListener { adapterView, view, i, l ->
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Cofirm Delete")
                alert.setMessage("Do you want to delete?")
                alert.setPositiveButton("Yes") { dialog, which ->
                    deleteData(
                        SharedPrefs.instance[USERID, String::class.java, ""],
                        arrayList[i].movieId
                    )
                    loadData(SharedPrefs.instance[USERID, String::class.java, ""])
                    adapter.notifyDataSetChanged()
                }
                alert.setNegativeButton("No", null)
                alert.show()

                true
            }
            lvWishList.setOnItemClickListener { adapterView, view, i, l ->
                val intent = Intent(this, SingleMovieActivity::class.java)
                intent.putExtra("id", Integer.parseInt(arrayList[i].movieId))
                startActivity(intent)
            }
        } else {
            showToast("You must login to have wish list")
        }

        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun deleteData(userId: String, movieId: String) {
        val resultDelete = dbHelper.deleteWishList(userId, movieId)
        if (resultDelete == 1L) {
            Toast.makeText(this, "Successful delete!", Toast.LENGTH_SHORT)
                .show()
        } else {

            Toast.makeText(this, "Error delete!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun loadData(id: String) {
        arrayList.clear()
        val cursor: Cursor = dbHelper.getWishList(id)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val idMovie: String = cursor.getString(0)
            val nameMovie: String = cursor.getString(1)
            val idUser: String = cursor.getString(2)
            val fv = Favourite(idMovie, nameMovie, idUser)
            arrayList.add(fv)
            cursor.moveToNext()
        }
    }
}
