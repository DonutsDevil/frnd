package com.swapnil.frnd.utility.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.frnd.R
import java.time.LocalDate

class CalendarAdapter(): ListAdapter<LocalDate, CalendarAdapter.Companion.CalendarHolder>(CalendarDiffUtil()) {

    companion object {
        class CalendarHolder(view: View): RecyclerView.ViewHolder(view) {
            val tvDayOfMonth: TextView
            private val parentView: View
            init {
                tvDayOfMonth = view.findViewById(R.id.tv_date)
                parentView = view.findViewById(R.id.cl_parent_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date = getItem(position)
        if(date == null)
            holder.tvDayOfMonth.text = ""
        else
        {
            holder.tvDayOfMonth.text = date.dayOfMonth.toString()
           /* if(date.equals(CalendarUtils.selectedDate))
                holder.parentView.setBackgroundColor(Color.LTGRAY);*/
        }
    }
}

class CalendarDiffUtil: DiffUtil.ItemCallback<LocalDate>() {
    override fun areItemsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
        return oldItem == newItem
    }

}