package com.swapnil.frnd.repository.network.serviceImpl

import android.util.Log
import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.model.network.response.TaskStatus
import com.swapnil.frnd.repository.network.service.TaskAPI
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import com.swapnil.frnd.utility.BaseConstants.Companion.Status
import retrofit2.Response
import javax.inject.Inject

class TaskNetworkServiceImpl @Inject constructor(private val taskApi: TaskAPI) :
    TaskNetworkService {

    private val TAG = "TaskNetworkServiceImpl"
    override suspend fun getTasks(taskRequest: TaskRequest): Status<TaskResponse> {
        val response = taskApi.getAllTasks(taskRequest)
        return getResponseStatus(response)
    }

    override suspend fun postTask(taskPostRequest: TaskPostRequest): Status<TaskStatus> {
        val response = taskApi.postTask(taskPostRequest)
        return getResponseStatus(response)
    }

    override suspend fun deleteTask(taskDeleteRequest: TaskDeleteRequest): Status<TaskStatus> {
        val response = taskApi.deleteTask(taskDeleteRequest)
        return getResponseStatus(response)
    }

    private fun <T> getResponseStatus(response: Response<T>?): Status<T> {
        response?.let { _response ->
            if (_response.isSuccessful) {
                _response.body()?.let { it ->
                    return Status.Success(it)
                } ?: Log.d(TAG, "getResponseStatus: body is null")
            } else {
                Log.d(
                    TAG,
                    "getResponseStatus: request is was unsuccessful, ${
                        _response.errorBody()?.string()
                    }"
                )
            }
        }
        val errorMessage = "Something Went Wrong, Try again later"
        return Status.Error(errorMessage)
    }
}