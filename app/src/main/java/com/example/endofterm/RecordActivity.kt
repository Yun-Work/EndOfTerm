package com.example.endofterm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.*

class RecordActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var barChart: BarChart
    private lateinit var spinnerYear: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        spinnerYear = findViewById(R.id.spinner_year)
        spinnerMonth = findViewById(R.id.spinner_month)
        barChart = findViewById(R.id.barChart)
        recyclerView = findViewById(R.id.recyclerView_symptoms)
        textNoData = findViewById(R.id.text_no_data)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val yearList = (2020..2050).map { it.toString() }
        val monthList = (1..12).map { String.format("%02d", it) }

        spinnerYear.adapter = ArrayAdapter(this, R.layout.spinner_item, yearList)
        spinnerMonth.adapter = ArrayAdapter(this, R.layout.spinner_item, monthList)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR).toString()
        val currentMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)

        spinnerYear.setSelection(yearList.indexOf(currentYear))
        spinnerMonth.setSelection(monthList.indexOf(currentMonth))

        val updateData: () -> Unit = {
            val selectedYear = spinnerYear.selectedItem.toString().toInt()
            val selectedMonth = spinnerMonth.selectedItem.toString().toInt()
            fetchSymptomSummary(selectedYear, selectedMonth)
            fetchSymptomLog(selectedYear, selectedMonth)
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) = updateData()
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) = updateData()
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        updateData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchSymptomSummary(year: Int, month: Int) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/api/symptoms/summary?year=$year&month=$month")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Log.e("RecordActivity", "GET summary 失敗", e) }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val list = mutableListOf<SymptomSummary>()
                    val jsonArray = JSONArray(body)

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        list.add(SymptomSummary(obj.getString("name"), obj.getInt("count")))
                    }

                    runOnUiThread {
                        if (list.isEmpty()) {
                            barChart.clear()
                            barChart.invalidate()
                            textNoData.visibility = View.VISIBLE
                        } else {
                            textNoData.visibility = View.GONE
                            drawBarChart(list)
                        }
                    }
                } else {
                    Log.e("RecordActivity", "GET summary 錯誤：${response.code}")
                }
            }
        })
    }

    private fun fetchSymptomLog(year: Int, month: Int) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/api/symptoms/logs?year=$year&month=$month")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Log.e("RecordActivity", "GET logs 失敗", e) }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val logList = mutableListOf<SymptomLogItem>()
                    val jsonArray = JSONArray(body)

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        logList.add(SymptomLogItem(obj.getString("date"), obj.getString("name")))
                    }

                    runOnUiThread {
                        recyclerView.adapter = SymptomLogAdapter(logList)
                    }
                }
            }
        })
    }

    private fun drawBarChart(list: List<SymptomSummary>) {
        val entries = list.mapIndexed { i, item -> BarEntry(i.toFloat(), item.count.toFloat()) }

        val dataSet = BarDataSet(entries, "症狀次數").apply {
            valueTextSize = 16f
            color = resources.getColor(R.color.teal_200, theme)
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(entry: BarEntry?): String = String.format("%.0f", entry?.y ?: 0f)
            }
        }

        barChart.data = BarData(dataSet).apply { barWidth = 0.6f }

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(list.map { it.name })
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
            labelRotationAngle = 0f
            textSize = 16f
            labelCount = list.size
        }

        barChart.axisLeft.apply {
            textSize = 14f
            granularity = 1f
            setLabelCount(5, false)
            axisMinimum = 0f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = value.toInt().toString()
            }
        }

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setViewPortOffsets(30f, 10f, 30f, 70f)
        barChart.setFitBars(true)
        barChart.setVisibleXRangeMaximum(3f)
        barChart.moveViewToX(0f)
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
