package io.github.takusan23.schoolassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.takusan23.schoolassist.Fragment.AllTimeTableFragment
import io.github.takusan23.schoolassist.Fragment.PreferenceFragment
import io.github.takusan23.schoolassist.Fragment.TimeTableFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                R.id.main_activity_bottom_nav_setting_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_linearlayout_fragment, PreferenceFragment()).commit()
                    supportActionBar?.title = "設定"
                }
            }
            true
        }

    }
}
