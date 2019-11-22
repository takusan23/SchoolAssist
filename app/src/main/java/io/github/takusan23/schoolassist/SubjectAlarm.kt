package io.github.takusan23.schoolassist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import io.github.takusan23.schoolassist.Receiver.AlarmManagerReceiver
import java.util.*

class SubjectAlarm(val context: Context?) {

    val intentList = arrayListOf<Intent>()
    val repeatIntent = Intent(context, AlarmManagerReceiver::class.java).apply {
        putExtra("set", true)
    }

    init {
        for (i in 0 until 6) {
            val intent = Intent(context, AlarmManagerReceiver::class.java)
            intent.putExtra("subject", i)
            intentList.add(intent)
        }
    }

    fun init() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pref_setting = PreferenceManager.getDefaultSharedPreferences(context)
        for (i in 0 until 6) {
            val hour = pref_setting.getString("${i}_start_hour", "0")?.toInt() ?: 0
            val minute = pref_setting.getString("${i}_start_minute", "0")?.toInt() ?: 0

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                add(Calendar.MINUTE, -5)
            }

            //AlarmManager
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    i,
                    intentList[i],
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    fun allCancel() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //全Cancel
        for (i in 0 until 6) {
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    i,
                    intentList[i],
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)
        }
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                100,
                repeatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmManager.cancel(pendingIntent)

    }

    //日が変わったら再登録するAlarmManagerを登録
    fun setOneTimeRegister() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                100,
                repeatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        //次の日を計算
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.add(Calendar.DATE, 1)
        //一日ごとに再登録
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            86400000L,
            pendingIntent
        )
    }

}