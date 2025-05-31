package com.example.endofterm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private fun openDetail(title: String, description: String, imageResIds: IntArray) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("TITLE", title)
        intent.putExtra("DESCRIPTION", description)
        intent.putExtra("IMAGE_RES_IDS", imageResIds)
        startActivity(intent)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(
            R.id.btn1).setOnClickListener {
            openDetail("後側頭痛", "按摩穴道:百會穴、風池穴\n按摩方式：雙手拇指按壓 10 秒、重複 5 次\n禁忌：高血壓患者需注意力道",intArrayOf(R.drawable.back_1_front, R.drawable.back_2))
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            openDetail("側面頭痛", "按摩穴道:率谷穴\n按摩方式：食指按壓，並輕揉 30 秒", intArrayOf(R.drawable.side))
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            openDetail("前額頭痛", "按摩穴道:神庭穴、頭維穴、上星\n按摩方式：指腹按壓、畫圓按摩 1 分鐘", intArrayOf(R.drawable.front_3, R.drawable.front_1,R.drawable.front_2))
        }

        findViewById<Button>(R.id.btn4).setOnClickListener {
            openDetail("頭暈", "按摩穴道:風池穴、神庭穴、率谷穴\n按摩方式：指壓 + 呼吸調節", intArrayOf(R.drawable.dizzy_1, R.drawable.dizzy_2,R.drawable.dizzy_3))
        }

        findViewById<Button>(R.id.btn5).setOnClickListener {
            openDetail("淡化黑眼圈", "按摩穴道:印堂穴、魚腰穴、晴明穴、攢竹穴\n按摩方式：每天早晚各按 1~2 分鐘", intArrayOf(R.drawable.dark_1, R.drawable.dark_2,R.drawable.dark_3,R.drawable.dark_4))
        }

        findViewById<Button>(R.id.btn6).setOnClickListener {
            openDetail("改善失眠", "按摩穴道:百會穴、風池穴\n按摩方式：睡前溫和按摩穴道", intArrayOf(R.drawable.sleep_1, R.drawable.sleep_2))
        }

        findViewById<Button>(R.id.btn7).setOnClickListener {
            openDetail("眼睛痠痛", "按摩穴道:承泣穴、魚腰穴、晴明穴、絲竹空穴、瞳子膠穴、攢竹穴\n按摩方式：用中指輕壓並做深呼吸",intArrayOf(R.drawable.eye_1, R.drawable.eye_2,R.drawable.eye_3,R.drawable.eye_4, R.drawable.eye_5,R.drawable.eye_6))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // 可以保持目前頁面，不用跳轉
                    true
                }
                R.id.menu_record -> {
                    val intent = Intent(this, RecordActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}




