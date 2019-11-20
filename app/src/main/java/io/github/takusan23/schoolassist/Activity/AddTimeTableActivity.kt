package io.github.takusan23.schoolassist.Activity

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import io.github.takusan23.schoolassist.DataBase.TimeTableSQLiteHelper
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.activity_add_time_table.*
import org.json.JSONArray

class AddTimeTableActivity : AppCompatActivity() {

    lateinit var timeTableSQLiteHelper: TimeTableSQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase

    val textInputList = arrayListOf<TextInputEditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_time_table)

        textInputList.add(add_timetable_one_text)
        textInputList.add(add_timetable_two_text)
        textInputList.add(add_timetable_tree_text)
        textInputList.add(add_timetable_four_text)
        textInputList.add(add_timetable_five_text)
        textInputList.add(add_timetable_six_text)
        textInputList.add(add_timetable_after_text)

        //初期化
        timeTableSQLiteHelper = TimeTableSQLiteHelper(this)
        sqLiteDatabase = timeTableSQLiteHelper.writableDatabase
        timeTableSQLiteHelper.setWriteAheadLoggingEnabled(false)

        //読み込む
        loadTimeTable("月")
        add_time_table_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                saveTimeTable(tab?.text as String? ?: "月")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                loadTimeTable(tab?.text as String? ?: "月")
            }
        })

    }


    fun loadTimeTable(week: String) {
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
            val json = JSONArray(cursor.getString(0))
            for (i in 0 until json.length()) {
                textInputList[i].setText(json.getString(i))
            }
        } else {
            //前のが残らないように
            textInputList.forEach {
                it.setText("")
            }
        }
        cursor.close()
    }

    fun saveTimeTable(s: String) {
        //すべてほぞｎ
        val jsonArray = JSONArray()
        textInputList.forEach {
            jsonArray.put(it.text.toString())
        }
        //書き込む
        val contentValues = ContentValues()
        contentValues.put("week", s)
        contentValues.put("subject", jsonArray.toString())
        val id = sqLiteDatabase.insertWithOnConflict(
            "timetable_db",
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_IGNORE
        ).toInt()
        //UpdateかInsertか
        val cursor = sqLiteDatabase.query(
            "timetable_db",
            arrayOf("subject"),
            "week=?",
            arrayOf(s),
            null,
            null,
            null
        )
        if (cursor.count != 0) {
            sqLiteDatabase.update("timetable_db", contentValues, "week=?", arrayOf(s))
        } else {
            sqLiteDatabase.insert("timetable_db", null, contentValues)
        }
        cursor.close()
    }


}
