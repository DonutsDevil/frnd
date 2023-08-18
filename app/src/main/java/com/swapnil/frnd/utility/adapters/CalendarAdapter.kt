package com.swapnil.frnd.utility.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.frnd.R
import java.time.LocalDate

class CalendarAdapter(
    private var selectedDate: LocalDate,
    private val onDateChangeListener: OnDateChangeListener,
    private var daysList: List<LocalDate?>
) :
    RecyclerView.Adapter<CalendarAdapter.Companion.CalendarHolder>() {

    companion object {
        class CalendarHolder(
            view: View,
            private val onDateChangeListener: OnDateChangeListener,
            private val daysList: List<LocalDate?>,
        ) :
            RecyclerView.ViewHolder(view) {
            private val tvDayOfMonth: TextView
            private val parentView: View

            init {
                tvDayOfMonth = view.findViewById(R.id.tv_date)
                parentView = view.findViewById(R.id.cl_parent_item)

                parentView.setOnClickListener {
                    val selectedDate = daysList[adapterPosition]
                    selectedDate?.let {
                        onDateChangeListener.onSelectedDateChange(it)
                    }
                }
            }

            fun setDayOfMonth(day: String) {
                tvDayOfMonth.text = day
            }

            fun setSelectedParentBackground(color: Int) {
                parentView.setBackgroundColor(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarHolder(view, onDateChangeListener, daysList)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date = daysList[position]
        if (date == null)
            holder.setDayOfMonth("")
        else {
            holder.setDayOfMonth(date.dayOfMonth.toString())
            if (date == selectedDate)
                holder.setSelectedParentBackground(Color.CYAN)
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
        notifyDataSetChanged()
    }

    fun submitList(daysList: List<LocalDate?>) {
        this.daysList = daysList
        notifyDataSetChanged()
    }
}

interface OnDateChangeListener {
    fun onSelectedDateChange(selectedDate: LocalDate)
}