package com.swapnil.frnd.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class EventsViewModel: ViewModel() {

    private var selectedDate :LocalDate

    private val _selectedMonthYear = MutableLiveData<String>()
    val selectedMonthYear: LiveData<String>
        get() = _selectedMonthYear

    private val _daysList = MutableLiveData<List<LocalDate?>>()
    val dayList: LiveData<List<LocalDate?>>
        get() = _daysList


    init {
        selectedDate = LocalDate.now()
        setMonthView()
    }


    private fun setMonthView() {
        val daysInMonth = daysInMonthArray(selectedDate)
        _selectedMonthYear.postValue(getMonthYearOfSelectedDate())
        _daysList.postValue(daysInMonth)
    }


    private fun getMonthYearOfSelectedDate(): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return selectedDate.format(formatter)
    }


    private fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        val daysInMonthList: ArrayList<LocalDate?> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (cell in 1..42) {
            if (cell <= dayOfWeek || cell > daysInMonth + dayOfWeek) {
                daysInMonthList.add(null)
            } else {
                daysInMonthList.add(LocalDate.of(yearMonth.year, yearMonth.month, cell - dayOfWeek))
            }
        }
        return daysInMonthList
    }

    fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

}