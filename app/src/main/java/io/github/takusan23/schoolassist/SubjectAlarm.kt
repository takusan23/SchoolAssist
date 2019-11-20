package io.github.takusan23.schoolassist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import io.github.takusan23.schoolassist.Receiver.AlarmManagerReceiver
import java.util.*

class SubjectAlarm(val context: Context?) {

    fun init() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //全Cancel
        //  for (i in 0 until 6) {
        //      val intent = Intent(context, AlarmManagerReceiver::class.java)
        //      val pendingIntent =
        //          PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //      pendingIntent.cancel()
        //      alarmManager.cancel(pendingIntent)
        //  }

        val pref_setting = PreferenceManager.getDefaultSharedPreferences(context)
        for (i in 0 until 6) {
            val hour = pref_setting.getString("${i}_start_hour", "0")?.toInt() ?: 0
            val minute = pref_setting.getString("${i}_start_minute", "0")?.toInt() ?: 0

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            //AlarmManager
            val intent = Intent(context, AlarmManagerReceiver::class.java)
            intent.putExtra("subject", i)
            val pendingIntent =
                PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
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

}