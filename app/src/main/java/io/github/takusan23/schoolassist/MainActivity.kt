package io.github.takusan23.schoolassist

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import io.github.takusan23.schoolassist.DataBase.TimeTableSQLiteHelper
import io.github.takusan23.schoolassist.Fragment.AllTimeTableFragment
import io.github.takusan23.schoolassist.Fragment.PreferenceFragment
import io.github.takusan23.schoolassist.Fragment.QRFragment
import io.github.takusan23.schoolassist.Fragment.TimeTableFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    lateinit var timeTableSQLiteHelper: TimeTableSQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初期化
        timeTableSQLiteHelper = TimeTableSQLiteHelper(this)
        sqLiteDatabase = timeTableSQLiteHelper.writableDatabase
        timeTableSQLiteHelper.setWriteAheadLoggingEnabled(false)

        //じかんわり
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_linearlayout_fragment, TimeTableFragment()).commit()
        supportActionBar?.title = "今日"

        //BottomNavBarおしたとき
        activity_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_activity_bottom_nav_today_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_linearlayout_fragment, TimeTableFragment()).commit()
                    supportActionBar?.title = "今日"
                }
                R.id.main_activity_bottom_nav_table_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_linearlayout_fragment, AllTimeTableFragment())
                        .commit()
                    supportActionBar?.title = "全表示"
                }
                R.id.main_activity_bottom_qr_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_linearlayout_fragment, QRFragment()).commit()
                    supportActionBar?.title = "QRコード"
                }
                R.id.main_activity_bottom_nav_setting_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_linearlayout_fragment, PreferenceFragment()).commit()
                    supportActionBar?.title = "設定"
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //内容が上書きされる
            saveTimeTable(result.contents)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun saveTimeTable(jsonString: String) {
        //すべてほぞｎ
        val jsonArray = JSONArray(jsonString)
        val list = arrayListOf("月", "火", "水", "木", "金")
        for (i in 0 until jsonArray.length()) {
            val subjectArray = jsonArray.getJSONArray(i)
            val dayOfWeek = list[i]
            //書き込む
            val contentValues = ContentValues()
            contentValues.put("week", dayOfWeek)
            contentValues.put("subject", subjectArray.toString())
            //UpdateかInsertか
            val cursor = sqLiteDatabase.query(
                "timetable_db",
                arrayOf("subject"),
                "week=?",
                arrayOf(dayOfWeek),
                null,
                null,
                null
            )
            if (cursor.count != 0) {
                sqLiteDatabase.update("timetable_db", contentValues, "week=?", arrayOf(dayOfWeek))
            } else {
                sqLiteDatabase.insert("timetable_db", null, contentValues)
            }
            Toast.makeText(this,dayOfWeek+"曜日の取り込み成功",Toast.LENGTH_SHORT).show()
            cursor.close()
        }
    }

}
