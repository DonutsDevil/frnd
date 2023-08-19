package com.swapnil.frnd.repository

import android.content.Context
import android.util.Log
import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.model.network.response.TaskStatus
import com.swapnil.frnd.repository.local.service.TaskLocalService
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import com.swapnil.frnd.utility.BaseConstants
import com.swapnil.frnd.utility.BaseConstants.Companion.Status
import com.swapnil.frnd.utility.Utility
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskNetworkService: TaskNetworkService,
    private val taskLocalService: TaskLocalService,
    private val context: Context
) {
    private val TAG = "TaskRepository"
    suspend fun getTaskFor(taskRequest: TaskRequest): Status<TaskResponse> {
        if (isPreconditionSuccess().not()) {
            return Status.Error(BaseConstants.Error.NO_INTERNET.msg)
        }
        val status = taskNetworkService.getTasks(taskRequest)
        return processGetTaskResult(status)
    }

    suspend fun postTask(taskPostRequest: TaskPostRequest): Status<TaskResponse> {
        if (isPreconditionSuccess().not()) {
            return Status.Error(BaseConstants.Error.NO_INTERNET.msg)
        }
        val status = taskNetworkService.postTask(taskPostRequest)
        return getTaskResponseStatus(status, taskPostRequest.userId)
    }

    suspend fun deleteTask(taskDeleteRequest: TaskDeleteRequest): Status<TaskResponse> {
        if (isPreconditionSuccess().not()) {
            return Status.Error(BaseConstants.Error.NO_INTERNET.msg)
        }
        val status = taskNetworkService.deleteTask(taskDeleteRequest)
        if (status is Status.Success) {
            val count = taskLocalService.deleteTask(taskDeleteRequest.taskId)
            Log.d(TAG, "deleteTask: delete taskId from local, delete count: $count")
        }
        return getTaskResponseStatus(status, taskDeleteRequest.userId)
    }

    private fun isPreconditionSuccess(): Boolean {
        return Utility.isInternetConnected(context)
    }

    private suspend fun <T> getTaskResponseStatus(
        status: Status<T>,
        userId: Int,
    ): Status<TaskResponse> {
        return when (status) {
            is Status.Error -> {
                Log.e(TAG, "postTask: error occurred: ${status.errorMsg}")
                Status.Error(status.errorMsg!!)
            }
            is Status.Success -> {
                Log.d(TAG, "postTask: status: ${status.data}")
                getTaskFor(TaskRequest(userId))
            }
            is Status.Loading -> Status.Loading()
        }
    }

    private suspend fun processGetTaskResult(status: Status<TaskResponse>) : Status<TaskResponse> {
        Log.d(TAG, "processGetTaskResult: status: $status")
        return when (status) {
            is Status.Error -> {
                // was error due to no internet post making the call?
                if (!Utility.isInternetConnected(context)) {
                    Status.Error(BaseConstants.Error.NO_INTERNET.msg)
                } else {
                    Log.e(TAG, "processGetTaskResult: error occurred: ${status.errorMsg}")
                    status
                }
            }
            is Status.Success -> {
                status.data?.let {
                    Log.d(TAG, "processGetTaskResult: list: ${status.data}")
                    taskLocalService.addTasks(it.taskList)
                    getTaskFromDb()
                } ?: status
            }
            else -> {
                status
            }
        }
    }

    suspend fun getTaskFromDb(): Status.Success<TaskResponse> {
        val list = taskLocalService.getTasks()
        val taskResponse = TaskResponse(list)
        return Status.Success(taskResponse)
    }
}