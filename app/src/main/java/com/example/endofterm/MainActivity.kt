package com.example.endofterm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class MainActivity : AppCompatActivity() {

    private fun openDetail(title: String, description: String, imageResIds: IntArray) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("TITLE", title)
        intent.putExtra("DESCRIPTION", description)
        intent.putExtra("IMAGE_RES_IDS", imageResIds)
        startActivity(intent)
    }

    private fun sendSymptomLogToServer(symptomName: String) {
        val url = "https://d550-2001-b011-4004-9252-94dc-aa65-49de-7de5.ngrok-free.app/api/symptoms"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)

        val json = JSONObject().apply {
            put("name", symptomName)
            put("date", today)
        }

        val requestBody = json.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())


        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "POST 失敗", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "POST 成功：${response.body?.string()}")
                } else {
                    Log.e("MainActivity", "POST 錯誤：${response.code}")
                }

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn1).setOnClickListener {
            val name = "後側頭痛"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:百會穴、風池穴\n\n" +
                    "1.百會穴:\n" +
                    "☝穴道位置：將兩耳尖高點向上連成一線，與鼻頭向上延伸的交會處。\n" +
                    "✌按摩方法：以拇指指腹 輕按，按壓5秒後停1秒 為1次，連續按壓7次。\n" +
                    "✔功效：安神、醒腦、開竅。\n\n"+"2.風池穴:\n" +
                    "☝穴道位置：枕外隆突下 方，後髮際線向上1寸。 \n" +
                    "✌按摩方法：以拇指指腹 輕按，按壓5秒後停1秒 為1次，持續1分鐘。 \n" +
                    "✔功效：舒緩疲勞，通關開竅\n", intArrayOf(R.drawable.back_1_front, R.drawable.back_2))
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            val name = "側面頭痛"
            sendSymptomLogToServer(name)
            openDetail("側面頭痛", "按摩穴道:率谷穴\n\n率谷穴:\n" +
                    "☝穴道位置：找到耳尖位置，向上兩手指寬處。 \n" +
                    "✌按摩方法：頭痛期間以手指指腹按壓5秒後停 1秒為1次，連續按壓7 次。\n" +
                    "✔功效：祛除風熱。", intArrayOf(R.drawable.side))
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            val name = "前額頭痛"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:頭維穴\n\n頭維穴:\n" +
                    "☝穴道位置：頭側部，額角髮際上0.5寸，頭正中線旁4.5寸。 \n" +
                    "✌按摩方法：中指指腹輕揉迴旋按摩，力度適中，不是深力度按壓，每次施治時間1～3分 鐘，每日2～3次。 \n" +
                    "✔功效：瀉火止痛、緩解 疲勞。\n", intArrayOf( R.drawable.front_1))
        }

        findViewById<Button>(R.id.btn4).setOnClickListener {
            val name = "頭暈"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:風池穴、神庭穴、率谷穴、翳風穴\n\n率谷穴:\n" +
                    "☝穴道位置：找到耳尖位置，向上兩手指寬處。 \n" +
                    "✌按摩方法：用雙手的中指指腹按壓在率谷穴上，按壓3-5分鐘。\n" +
                    "✔功效：緩解頭暈、眩暈。\n\n" +
                    "風池穴:\n" +
                    "☝穴道位置：頸部頭骨下，兩條大筋外緣陷窩中，相當於耳垂齊平。 \n" +
                    "✌按摩方法：雙手大拇指各自放到頸部風池穴，其餘四指輕拂頭部，大拇指由輕到重輕按風池穴，約按 20～30 次。\u200B\n" +
                    "✔功效：緩解頭痛與頭暈\n\n"+"神庭穴:\n" +
                    "☝穴道位置：位於把身體切成左右兩半，畫一條中線，跟髮際線交界處再往後約拇指一半位置。 \n" +
                    "✌按摩方法：用單手拇指按壓神庭穴三次，再以順時針旋轉按摩三次。\n" +
                    "✔功效：寧神醒腦。\n\n" +
                    "翳風穴:\n" +
                    "☝穴道位置： 耳垂後，乳突與下頜骨間的凹陷處。\n" +
                    "✌按摩方法：用拇指指腹，小幅度旋轉畫圈揉動，按摩 3 – 5 分鐘。\n" +
                    "✔功效：調整內耳的平衡，減輕頭暈不適", intArrayOf(R.drawable.dizzy_1, R.drawable.dizzy_2,R.drawable.dizzy_3,R.drawable.dizzy_new))
        }

        findViewById<Button>(R.id.btn5).setOnClickListener {
            val name = "淡化黑眼圈"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:印堂穴、魚腰穴、晴明穴、攢竹穴\n\n魚腰穴:\n" +
                    "☝穴道位置： 位於眉毛中央\n" +
                    "✌按摩方法：用指關節順時針輕輕按壓5圈，重覆20-30次。\n" +
                    "✔功效：鎮驚安神、疏風通絡\n\n" +
                    "攢竹穴:\n" +
                    "☝穴道位置： 位於眉頭\n" +
                    "✌按摩方法：用指關節順時針輕輕按壓5圈，重覆20-30次。\n" +
                    "✔功效：清熱明目，袪風通絡。\n\n" +
                    "晴明穴:\n" +
                    "☝穴道位置： 位於內眼角的鼻樑上\n" +
                    "✌按摩方法：用指關節順時針輕輕按壓5圈，重覆20-30次。\n" +
                    "✔功效：緩解眼睛疲勞、消除眼周皺紋。\n\n" +
                    "印堂穴:\n" +
                    "☝穴道位置： 位於兩眉頭的中心點\n" +
                    "✌按摩方法：可用指關節順時針輕輕按壓5圈，重覆20-30次。\n" +
                    "✔功效：幫助血液循環\n", intArrayOf(R.drawable.dark_1, R.drawable.dark_2,R.drawable.dark_3,R.drawable.dark_4))
        }

        findViewById<Button>(R.id.btn6).setOnClickListener {
            val name = "改善失眠"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:百會穴、風池穴\n\n百會穴:\n" +
                    "☝穴道位置： 頭頂正中央，從兩耳耳尖延伸向上與頭部正中線交叉處。\n" +
                    "✌按摩方法：以拇指按壓失眠穴位，也可用手掌心揉壓頭頂頭皮，並以順時針、逆時針方向輪流按摩，每次按摩1分鐘。\n" +
                    "✔功效：氣血循環，具有安神、緩減緊張、延年益壽、促進新陳代謝功效。\n\n" +
                    "風池穴:\n" +
                    "☝穴道位置： 腦後髮際線上與枕骨下的交界處，，將雙手掌心貼向耳朵，大拇指由髮際線向上摸，碰到的凹陷處就是風池穴的位置。\n" +
                    "✌按摩方法：用雙手拇指或指節按壓，每次按3-5分鐘，感覺助眠穴位出現痠脹感即可；也可以用溫毛巾熱敷此穴位。\n" +
                    "✔功效：促進循環", intArrayOf(R.drawable.sleep_1, R.drawable.sleep_2))
        }

        findViewById<Button>(R.id.btn7).setOnClickListener {
            val name = "眼睛痠痛"
            sendSymptomLogToServer(name)
            openDetail(name, "按摩穴道:承泣穴、魚腰穴、晴明穴、絲竹空穴、瞳子膠穴、攢竹穴\n\n攢竹穴:\n" +
                    "☝穴道位置： 眉頭\n" +
                    "✌按摩方法：使用雙手大拇指指腹按壓攢竹穴 3～5 秒，再放鬆，重複按壓數次。\n" +
                    "✔功效：明目醒腦，改善目眩刺痛，還有眼皮跳動的不舒服。\n\n" +
                    "魚腰穴:\n" +
                    "☝穴道位置： 眉中\n" +
                    "✌按摩方法：以食指指腹輕輕按壓，或小幅度來回推揉 10～20 次。\n" +
                    "✔功效：改善眼睛疲勞、頭痛。\n\n" +
                    "絲竹空穴:\n" +
                    "☝穴道位置： 眉尾\n" +
                    "" +
                    "" +
                    "按摩方法：以指腹順時針與逆時針各按摩 10 次，早晚各一次。\n" +
                    "✔功效：明目止痛，改善偏頭痛。\n\n" +
                    "瞳子膠穴:\n" +
                    "☝穴道位置： 眼尾\n" +
                    "✌按摩方法：以中指輕輕點壓或畫圈按摩 15～30 秒。\n" +
                    "✔功效：消除眼睛紅腫，預防近視，更能延緩眼瞼皮膚下垂保持年輕。\n\n" +
                    "睛明穴:\n" +
                    "☝穴道位置： 眼頭\n" +
                    "✌按摩方法：雙手食指同時按壓左右睛明穴，每次 5 秒，重複 10 次。\n" +
                    "✔功效：能夠降低眼壓並消除疲勞。\n\n" +
                    "承泣穴:\n" +
                    "☝穴道位置： 眼球正下方，眼框下緣中間\n" +
                    "✌按摩方法：用無名指輕柔按壓 3～5 秒，再放開，反覆 5～10 次。\n" +
                    "✔功效：眼睛紅痛時多按，能夠散風清熱",intArrayOf(R.drawable.eye_1, R.drawable.eye_2,R.drawable.eye_3,R.drawable.eye_4, R.drawable.eye_5,R.drawable.eye_6))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> true
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
