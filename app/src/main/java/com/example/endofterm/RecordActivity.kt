package com.example.endofterm

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.Calendar
import com.example.endofterm.SymptomSummary


class RecordActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        val spinnerYear: Spinner = findViewById(R.id.spinner_year)
        val spinnerMonth: Spinner = findViewById(R.id.spinner_month)
        barChart = findViewById(R.id.barChart)

        val yearList = (2020..2050).map { it.toString() }
        val monthList = (1..12).map { String.format("%02d", it) }

        val yearAdapter = ArrayAdapter(this, R.layout.spinner_item, yearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        val monthAdapter = ArrayAdapter(this, R.layout.spinner_item, monthList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter

        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val currentMonth = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1)

        spinnerYear.setSelection(yearList.indexOf(currentYear))
        spinnerMonth.setSelection(monthList.indexOf(currentMonth))

        fetchSymptomSummary()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_symptoms)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchSymptomSummary() {
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/api/symptoms/summary")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Log.e("RecordActivity", "GET 失敗", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val summaryList = mutableListOf<SymptomSummary>()

                    val jsonArray = JSONArray(body)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val name = obj.getString("name")
                        val count = obj.getInt("count")
                        summaryList.add(SymptomSummary(name, count))
                    }

                    runOnUiThread {
                        Log.d("RecordActivity", "取得成功：$summaryList")
                        drawBarChart(summaryList)

                        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_symptoms)
                        recyclerView.adapter = SymptomSummaryAdapter(summaryList)
                    }
                } else {
                    Log.e("RecordActivity", "GET 錯誤：${response.code}")
                }
            }
        })
    }

    private fun drawBarChart(symptomList: List<SymptomSummary>) {
        val entries = symptomList.mapIndexed { index, symptom ->
            BarEntry(index.toFloat(), symptom.count.toFloat())
        }

        val dataSet = BarDataSet(entries, "症狀次數")
        dataSet.valueTextSize = 16f
        dataSet.color = resources.getColor(R.color.teal_200, theme)

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f  // 👉 加這行讓 bar 寬一些
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(symptomList.map { it.name })
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -75f
        xAxis.textSize = 12f
        xAxis.labelCount = symptomList.size

        barChart.axisLeft.textSize = 14f
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.setVisibleXRangeMaximum(3f) // 👉 每次只顯示3條
        barChart.moveViewToX(0f)             // 👉 初始位置從左邊開始
        barChart.animateY(1000)
        barChart.invalidate()
    }

}
