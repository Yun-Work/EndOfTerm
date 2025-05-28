
package com.example.endofterm

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        // 取得從 MainActivity 傳來的資料
        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")
        val imageResIds = intent.getIntArrayExtra("IMAGE_RES_IDS") ?: intArrayOf()
        // 對應畫面元件
        val titleText = findViewById<TextView>(R.id.titleText)
        val descText = findViewById<TextView>(R.id.descText)
        val viewPager = findViewById<ViewPager2>(R.id.imagePager)
//        Log.d("DetailActivity", "title: $title")
//        Log.d("DetailActivity", "description: $description")
//        Log.d("DetailActivity", "imageResIds: ${imageResIds.joinToString()}")


        // 設定顯示內容
        titleText.text = title
        descText.text = description
        viewPager.adapter = ImageAdapter(imageResIds)
    }
}
