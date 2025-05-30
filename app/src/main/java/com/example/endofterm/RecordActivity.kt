
package com.example.endofterm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

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
