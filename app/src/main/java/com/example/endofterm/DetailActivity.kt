package com.example.endofterm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("TITLE")
        val desc = intent.getStringExtra("DESCRIPTION")
        val imageResIds = intent.getIntArrayExtra("IMAGE_RES_IDS") ?: intArrayOf()

        findViewById<TextView>(R.id.titleText).text = title
        findViewById<TextView>(R.id.descText).text = desc

        val viewPager = findViewById<ViewPager2>(R.id.imagePager)
        viewPager.adapter = ImageAdapter(imageResIds)

        // 啟用返回箭頭
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 或 finish()
        return true
    }

}
