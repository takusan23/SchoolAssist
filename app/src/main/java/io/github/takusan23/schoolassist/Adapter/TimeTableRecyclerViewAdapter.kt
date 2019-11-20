package io.github.takusan23.schoolassist.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.takusan23.schoolassist.R

class TimeTableRecyclerViewAdapter(private val arrayListArrayAdapter: ArrayList<ArrayList<String>>) :
    RecyclerView.Adapter<TimeTableRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_time_table, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListArrayAdapter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = arrayListArrayAdapter[position] as ArrayList<String>
        val subject = item.get(1)

        holder.subjectTextView.text = subject

        //なん時間目か
        if (position + 1 == 7) {
            holder.timeTextView.text = "放課後"
        } else {
            holder.timeTextView.text = "${position + 1} 時間目"
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var subjectTextView: TextView
        var timeTextView: TextView

        init {
            subjectTextView = itemView.findViewById(R.id.adapter_time_table_subject_textview)
            timeTextView = itemView.findViewById(R.id.adapter_time_table_time_textview)
        }
    }
}