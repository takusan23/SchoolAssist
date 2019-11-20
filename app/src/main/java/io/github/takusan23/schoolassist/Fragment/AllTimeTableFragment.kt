package io.github.takusan23.schoolassist.Fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.fragment_all_time_table.*
import org.w3c.dom.Text

class AllTimeTableFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_time_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //LinearLayout動的生成
        for (i in 0 until 5) {
            val list = arrayListOf("月", "火", "水", "木", "金")

            //曜日入れる
            val parentLinearLayout = LinearLayout(context)
            val textView = TextView(context)
            textView.text = "${list[i]}曜日"
            textView.gravity = Gravity.CENTER
            parentLinearLayout.orientation = LinearLayout.VERTICAL
            parentLinearLayout.addView(textView)

            val linearLayout = LinearLayout(context)
            //おおきさ
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.weight = 1F
            linearLayout.layoutParams = params
            parentLinearLayout.layoutParams = params
            linearLayout.id = View.generateViewId()

            parentLinearLayout.addView(linearLayout)
            fragment_all_main_linearlayout.addView(parentLinearLayout)

            val timeTableFragment = TimeTableFragment()
            val bundle = Bundle()
            bundle.putString("date", list[i])
            timeTableFragment.arguments = bundle

            fragmentManager?.beginTransaction()
                ?.replace(linearLayout.id, timeTableFragment)?.commit()
        }
    }
}