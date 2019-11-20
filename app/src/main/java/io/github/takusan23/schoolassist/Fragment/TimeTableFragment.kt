package io.github.takusan23.schoolassist.Fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.schoolassist.Adapter.TimeTableRecyclerViewAdapter
import io.github.takusan23.schoolassist.Activity.AddTimeTableActivity
import io.github.takusan23.schoolassist.DataBase.TimeTableSQLiteHelper
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.fragment_time_table.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

class TimeTableFragment : Fragment() {

    lateinit var timeTableRecyclerViewAdapter: TimeTableRecyclerViewAdapter
    val recyclerViewList = arrayListOf<ArrayList<String>>()

    lateinit var timeTableSQLiteHelper: TimeTableSQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView初期化
        fragment_time_table_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        fragment_time_table_recyclerview.layoutManager = layoutManager
        timeTableRecyclerViewAdapter = TimeTableRecyclerViewAdapter(recyclerViewList)
        fragment_time_table_recyclerview.adapter = timeTableRecyclerViewAdapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        fragment_time_table_recyclerview.addItemDecoration(itemDecoration)

        //データベース読み込み
        //初期化
        timeTableSQLiteHelper = TimeTableSQLiteHelper(context)
        sqLiteDatabase = timeTableSQLiteHelper.writableDatabase
        timeTableSQLiteHelper.setWriteAheadLoggingEnabled(false)

        if (arguments?.getString("date") == null) {
            //今日
            if (getDayOfWeek() != null) {
                loadTimeTable(getDayOfWeek())
            }
        } else {
            //Fragmentに値が渡されてる時
            val day = arguments?.getString("date")
            loadTimeTable(day)
            floatingActionButton.hide()
            fragment_time_table_today_linearlayout.visibility = View.GONE
        }



        floatingActionButton.setOnClickListener {
            //編集画面
            val intent = Intent(context, AddTimeTableActivity::class.java)
            startActivity(intent)
        }

        setToday()

    }

    //うえのカードの中身
    fun setToday() {
        Timer().schedule(timerTask {
            activity?.runOnUiThread {
                if (fragment_time_table_today_date != null && fragment_time_table_today_time != null) {
                    val dateSimpleDateFormat = SimpleDateFormat("MM/dd E曜日")
                    val timeSimpleDateFormat = SimpleDateFormat("HH:mm:ss")
                    fragment_time_table_today_date.text =
                        dateSimpleDateFormat.format(Date(System.currentTimeMillis()))
                    fragment_time_table_today_time.text =
                        timeSimpleDateFormat.format(Date(System.currentTimeMillis()))
                }
            }
        }, 1000L, 1000L)
    }

    fun loadTimeTable(week: String?) {
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
                val item = arrayListOf<String>()
                item.add("")
                item.add(json.getString(i))
                recyclerViewList.add(item)
            }
        } else {
            Toast.makeText(context, "未登録です", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
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