package io.github.takusan23.schoolassist.Activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import io.github.takusan23.schoolassist.R
import io.github.takusan23.schoolassist.Receiver.AlarmManagerReceiver
import io.github.takusan23.schoolassist.SubjectAlarm
import kotlinx.android.synthetic.main.activity_notification_setting.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


class NotificationSettingActivity : AppCompatActivity() {

    lateinit var pref_setting: SharedPreferences
    val textViewList = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_setting)

        pref_setting = PreferenceManager.getDefaultSharedPreferences(this)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        //ボタン動的生成
        for (i in 0 until 6) {
            val button = MaterialButton(this)
            button.text = "${i + 1}時間目の開始時間設定"

            val textView = TextView(this).apply {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.weight = 1F
                layoutParams = params
                text = ""
                textViewList.add(this)
            }

            val linearLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = params
                setPadding(10, 10, 10, 10)
            }

            linearLayout.addView(textView)
            linearLayout.addView(button)
            activity_notification_parent_linearlayout.addView(linearLayout)

            //時間ピッカー出す
            button.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        textView.text = "$hourOfDay:$minute"

                        val editor = pref_setting.edit()
                        editor.putString("${i}_start_hour", hourOfDay.toString())
                        editor.putString("${i}_start_minute", minute.toString())
                        editor.apply()

                    }, hour, minute, false
                )
                timePickerDialog.show()
            }
        }

        loadSetting()

    }

    fun loadSetting() {
        val timeList = pref_setting.getString("timelist", "") ?: ""
        if (timeList.isNotEmpty()) {
            val jsonArray = JSONArray(timeList)
            for (i in 0 until jsonArray.length()) {
                textViewList[i].text = jsonArray.getString(i)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveSetting()
    }

    fun saveSetting() {
        //全てに値があるか
        var allExists = true
        textViewList.forEach {
            if (it.text.isEmpty()) {
                allExists = false
            }
        }
        if (allExists) {
            val jsonArray = JSONArray()
            textViewList.forEach {
                jsonArray.put(it.text)
                val editor = pref_setting.edit()
                editor.putString("timelist", jsonArray.toString())
                editor.apply()
            }
            //AlarmManager再設定
            val subjectAlarm = SubjectAlarm(this)
            subjectAlarm.init()
        }
    }

}
