package com.swapnil.frnd

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.swapnil.frnd.di.component.DaggerMainActivityComponent
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.TaskDetails
import com.swapnil.frnd.utility.adapters.CalendarAdapter
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import com.swapnil.frnd.utility.adapters.TaskItemAdapter
import com.swapnil.frnd.viewmodel.EventsViewModel
import com.swapnil.frnd.viewmodel.EventsViewModelFactory
import java.time.LocalDate
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnDateChangeListener,
    TaskItemAdapter.CalendarEventsClickListener {

    private lateinit var rvCalendar: RecyclerView
    private lateinit var rvTaskItems: RecyclerView

    private lateinit var btnPreviousMonth: Button
    private lateinit var btnNextMonth: Button
    private lateinit var fabAddTask: FloatingActionButton

    private lateinit var tvSelectedMonthYear: TextView
    @Inject
    lateinit var calendarAdapter: CalendarAdapter
    @Inject
    lateinit var eventsViewModelFactory: EventsViewModelFactory
    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        val dao = (this.application as FrndApplication).db.getTaskDao()
        val retrofit = (this.application as FrndApplication).retrofit
        val mainActivityComponent = DaggerMainActivityComponent.factory().create(this, dao, retrofit)
        mainActivityComponent.inject(this)
        eventsViewModel = ViewModelProvider(this, eventsViewModelFactory)[EventsViewModel::class.java]
        setDaysListInCalendar()
        setUpCalendarRv()
        setUpCalendarTaskRv()
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
        rvTaskItems = findViewById(R.id.rv_task_items)

        btnPreviousMonth = findViewById(R.id.btn_previous_month)
        btnNextMonth = findViewById(R.id.btn_next_month)
        fabAddTask = findViewById(R.id.fab_addNewTask)

        tvSelectedMonthYear = findViewById(R.id.tv_monthYear)
    }

    private fun setUpCalendarRv() {
        rvCalendar.layoutManager = GridLayoutManager(this,7)
        rvCalendar.adapter = calendarAdapter
    }

    private fun setUpCalendarTaskRv() {
        val list = mutableListOf<Task>()
        for(index  in 0..10) {
            list.add(Task(taskDetail = TaskDetails("Title $index", "Description $index", "19-08-2023")))
        }
        val taskItemAdapter = TaskItemAdapter(this, list)
        rvTaskItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTaskItems.adapter = taskItemAdapter
        rvTaskItems.setHasFixedSize(false)
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

    override fun onDialogEditingClick(
        positiveDialogCallback: DialogInterface.OnClickListener?,
        position: Int,
        event: Task?
    ) {

    }

    override fun onDialogDeleteClick(
        deleteDialogCallback: DialogInterface.OnClickListener?,
        event: Task?,
        position: Int
    ) {

    }

}