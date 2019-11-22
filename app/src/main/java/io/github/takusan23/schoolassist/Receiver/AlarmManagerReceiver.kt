package io.github.takusan23.schoolassist.Receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.widget.Toast
import androidx.preference.PreferenceManager
import io.github.takusan23.schoolassist.DataBase.TimeTableSQLiteHelper
import io.github.takusan23.schoolassist.R
import io.github.takusan23.schoolassist.SubjectAlarm
import org.json.JSONArray
import java.util.*
import javax.security.auth.Subject

class AlarmManagerReceiver : BroadcastReceiver() {

    lateinit var pref_setting: SharedPreferences
    lateinit var timeTableSQLiteHelper: TimeTableSQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase

    var subject = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        pref_setting = PreferenceManager.getDefaultSharedPreferences(context)

        //初期化
        timeTableSQLiteHelper = TimeTableSQLiteHelper(context)
        sqLiteDatabase = timeTableSQLiteHelper.writableDatabase
        timeTableSQLiteHelper.setWriteAheadLoggingEnabled(false)

        //今日
        subject = intent?.getIntExtra("subject",0)?:0
        if (getDayOfWeek() != null) {
            loadTimeTable(context, getDayOfWeek())
        }

        Toast.makeText(context,subject.toString(),Toast.LENGTH_SHORT).show()

        val set = intent?.getBooleanExtra("set",false)?:false
        if(set){
            val subjectAlarm = SubjectAlarm(context)
            subjectAlarm.init()
        }
    }

    fun loadTimeTable(context: Context?, week: String?) {
        val cursor = sqLiteDatabase.query(
            "timetable_db",
            arrayOf("subject"),
            "week=?",
            arrayOf(week),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        //JSON
        if (cursor.count != 0) {
            val subject = JSONArray(cursor.getString(0)).getString(subject)
            //通知
            showNotification(context, subject)
        } else {
            Toast.makeText(context, "未登録です", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    fun showNotification(context: Context?, subjectTitle: String) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //通知チャンネル
        val id = "next_subject"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, "次の教科通知", NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager.getNotificationChannel(id) == null) {
                notificationManager.createNotificationChannel(channel)
            }
            val notification = Notification.Builder(context, id).apply {
                setContentText(subjectTitle)
                setContentTitle("${subject + 1}時間目の教科")
                setSmallIcon(R.drawable.ic_work_outline_24px)
            }.build()
            notificationManager.notify(2, notification)
        } else {
            val notification = Notification.Builder(context).apply {
                setContentText(subjectTitle)
                setContentTitle("${subject + 1}時間目の教科")
                setSmallIcon(R.drawable.ic_work_outline_24px)
            }.build()
            notificationManager.notify(2, notification)
        }
    }

    //今日の曜日を出す
    //休日はNullです
    fun getDayOfWeek(): String? {
        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val list = arrayListOf("", "月", "火", "水", "木", "金", "")
        if (list[week].isNotEmpty()) {
            return list[week]
        } else {
            return null
        }
    }
}