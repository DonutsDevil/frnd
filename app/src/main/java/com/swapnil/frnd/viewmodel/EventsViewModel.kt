package com.swapnil.frnd.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.TaskDetails
import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.repository.TaskRepository
import com.swapnil.frnd.utility.BaseConstants
import com.swapnil.frnd.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EventsViewModel(private val repository: TaskRepository) : ViewModel() {
    private val TAG = "EventsViewModel"
    private var selectedDate: LocalDate

    private val _selectedMonthYear = MutableLiveData<String>()
    val selectedMonthYear: LiveData<String>
        get() = _selectedMonthYear

    private val _daysList = MutableLiveData<List<LocalDate?>>()
    val dayList: LiveData<List<LocalDate?>>
        get() = _daysList

    private val dateTaskMap = mutableMapOf<String, MutableList<Task>>()

    private val _selectedDateTaskList = MutableLiveData<List<Task>>()
    val selectedDateTaskList: LiveData<List<Task>>
        get() = _selectedDateTaskList

    init {
        selectedDate = LocalDate.now()
        setMonthView()
        viewModelScope.launch {
            fetchAndSetSelectedDateTask()
        }
    }

    private suspend fun fetchTasksFromRepository() {
        when (val status = repository.getTaskFor(TaskRequest(Utility.getUserId()))) {
            is BaseConstants.Companion.Status.Error -> Log.d(
                TAG,
                "fetchTasksFromRepository: Failure Occurred when fetch all task"
            )
            is BaseConstants.Companion.Status.Loading -> { /* do nothing */
            }
            is BaseConstants.Companion.Status.Success -> processStatus(status)
        }

    }

    private fun setTasksForSelectedDate() {
        val dateKey = getDayMonthAndYearKey()
        val taskList = dateTaskMap[dateKey]
        val list = taskList ?: listOf()
        _selectedDateTaskList.postValue(list)
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

    private fun getDayMonthAndYearKey(): String? {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return selectedDate.format(formatter)
    }

    fun isValidDateFormat(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(dateString)
            true
        } catch (e: ParseException) {
            false
        }
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
        setTasksForSelectedDate()
    }

    private fun processStatus(status: BaseConstants.Companion.Status.Success<TaskResponse>) {
        val taskResponse = status.data
        taskResponse?.let {
            val taskList = it.taskList
            dateTaskMap.clear()
            Log.d(TAG, "processStatus: responseList: $taskList, map: $dateTaskMap")

            for (task in taskList) {
                if (task.taskDetail?.date?.isEmpty() == false) {
                    val value = dateTaskMap[task.taskDetail.date]
                    val list = value ?: mutableListOf()
                    list.add(task)
                    dateTaskMap[task.taskDetail.date] = list
                }
            }
            Log.d(TAG, "processStatus: post map: $dateTaskMap")
        }
    }

    fun postTask(title: String, description: String, date: String) {
        val taskDetails = TaskDetails(title, description, date)
        val taskPostRequest = TaskPostRequest(Utility.getUserId(), taskDetails)
        viewModelScope.launch(Dispatchers.IO) {
            repository.postTask(taskPostRequest)
            fetchAndSetSelectedDateTask()
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            // We can do editing of task here.
        }
    }

    fun deleteTask(task: Task) {
        task.taskId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val taskDeleteRequest = TaskDeleteRequest(Utility.getUserId(), task.taskId)
                Log.d(TAG, "deleteTask: task to be delete: $taskDeleteRequest")
                repository.deleteTask(taskDeleteRequest)
                fetchAndSetSelectedDateTask()
            }
        } ?: Log.e(TAG, "deleteTask: task id is null")
    }

    private suspend fun fetchAndSetSelectedDateTask() {
        fetchTasksFromRepository()
        setTasksForSelectedDate()
    }
}

class EventsViewModelFactory @Inject constructor(private val taskRepository: TaskRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventsViewModel(taskRepository) as T
    }
}