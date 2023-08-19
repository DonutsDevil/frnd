package com.swapnil.frnd.repository

import android.util.Log
import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.model.network.response.TaskStatus
import com.swapnil.frnd.repository.local.service.TaskLocalService
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import com.swapnil.frnd.utility.BaseConstants.Companion.Status
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskNetworkService: TaskNetworkService,
    private val taskLocalService: TaskLocalService
) {
    private val TAG = "TaskRepository"
    suspend fun getTaskFor(taskRequest: TaskRequest): Status<TaskResponse> {
        val status = taskNetworkService.getTasks(taskRequest)
        when (status) {
            is Status.Error -> Log.e(TAG, "getTaskFor: error occurred: ${status.errorMsg}")
            is Status.Loading -> TODO()
            is Status.Success -> Log.d(TAG, "getTaskFor: list: ${status.data}")
        }
        return status
    }

    suspend fun postTask(taskPostRequest: TaskPostRequest): Status<TaskStatus> {
        val status = taskNetworkService.postTask(taskPostRequest)
        when (status) {
            is Status.Error -> Log.e(TAG, "postTask: error occurred: ${status.errorMsg}")
            is Status.Loading -> TODO()
            is Status.Success -> Log.d(TAG, "postTask: status: ${status.data}")
        }
        return status
    }

    suspend fun deleteTask(taskDeleteRequest: TaskDeleteRequest): Status<TaskStatus> {
        val status = taskNetworkService.deleteTask(taskDeleteRequest)
        when (status) {
            is Status.Error -> Log.e(TAG, "deleteTask: error occurred: ${status.errorMsg}")
            is Status.Loading -> TODO()
            is Status.Success -> Log.d(TAG, "deleteTask: status: ${status.data}")
        }
        return status
    }
}