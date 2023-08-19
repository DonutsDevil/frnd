package com.swapnil.frnd.viewmodel

import androidx.lifecycle.*
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.TaskDetails
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.repository.TaskRepository
import com.swapnil.frnd.utility.BaseConstants
import com.swapnil.frnd.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventsViewModel(private val repository: TaskRepository) : ViewModel() {

    private var selectedDate: LocalDate

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
        return isFirstWeekStartingFromSecondWeek(daysInMonthList)
    }

    /**
     * This method returns 35 size list which remove the first week since it is null.
     */
    private fun isFirstWeekStartingFromSecondWeek(daysInMonthList: ArrayList<LocalDate?>): ArrayList<LocalDate?> {
        var isStartingFromSecondWeek = true

        for (index in 0..6) {
            if (daysInMonthList[index] != null) {
                isStartingFromSecondWeek = false
            }
        }

        if (!isStartingFromSecondWeek) {
            return daysInMonthList
        }

        val dayList = daysInMonthList.toMutableList()

        for (index in 0..6) {
            dayList.removeAt(index)
        }

        return ArrayList(dayList)
    }

    fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    /**
     * Only set the currentViewing Month date.
     */
    fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTaskFor(TaskRequest(Utility.getUserId()))
            postTask("Something is there", "Nai Bolna hai par",  "19-08-2023")
        }

    }

    fun postTask(title: String, description: String, date: String) {
        val taskDetails = TaskDetails(title, description, date)
        val taskPostRequest = TaskPostRequest(Utility.getUserId(), taskDetails)
        viewModelScope.launch {
            repository.postTask(taskPostRequest)
        }
    }

    fun getSelectedDate(): LocalDate {
        return selectedDate
    }
}

class EventsViewModelFactory @Inject constructor(private val taskRepository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventsViewModel(taskRepository) as T
    }
}