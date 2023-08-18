package com.swapnil.frnd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.frnd.di.component.DaggerMainActivityComponent
import com.swapnil.frnd.utility.adapters.CalendarAdapter
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import com.swapnil.frnd.viewmodel.EventsViewModel
import java.time.LocalDate
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnDateChangeListener {

    private lateinit var rvCalendar: RecyclerView
    private lateinit var btnPreviousMonth: Button
    private lateinit var btnNextMonth: Button
    private lateinit var tvSelectedMonthYear: TextView
    @Inject
    lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        eventsViewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        val mainActivityComponent = DaggerMainActivityComponent.factory().create(eventsViewModel.getSelectedDate(), this)
        mainActivityComponent.inject(this)
        setDaysListInCalendar()
        setUpCalendarRv()
        setMonthYear()

        btnNextMonth.setOnClickListener {
            eventsViewModel.nextMonthAction()
        }

        btnPreviousMonth.setOnClickListener {
            eventsViewModel.previousMonthAction()
        }

    }

    private fun initView() {
        rvCalendar = findViewById(R.id.rv_calendar_view)
        btnPreviousMonth = findViewById(R.id.btn_previous_month)
        btnNextMonth = findViewById(R.id.btn_next_month)
        tvSelectedMonthYear = findViewById(R.id.tv_monthYear)
    }

    private fun setUpCalendarRv() {
        rvCalendar.layoutManager = GridLayoutManager(this,7)
        rvCalendar.adapter = calendarAdapter
    }

    private fun setDaysListInCalendar() {
        eventsViewModel.dayList.observe(this) {
            calendarAdapter.submitList(it)
        }
    }
    private fun setMonthYear() {
        eventsViewModel.selectedMonthYear.observe(this) { monthYear ->
            tvSelectedMonthYear.text = monthYear
        }
    }

    override fun onSelectedDateChange(selectedDate: LocalDate) {
        calendarAdapter.updateSelectedDate(selectedDate)
        eventsViewModel.updateSelectedDate(selectedDate)
    }

}