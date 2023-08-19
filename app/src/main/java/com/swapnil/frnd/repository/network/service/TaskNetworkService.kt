package com.swapnil.frnd.repository.network.service

import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.model.network.response.TaskStatus
import com.swapnil.frnd.utility.BaseConstants.Companion.Status

interface TaskNetworkService {
    suspend fun getTasks(taskRequest: TaskRequest): Status<TaskResponse>
    suspend fun postTask(taskPostRequest: TaskPostRequest): Status<TaskStatus>
    suspend fun deleteTask(taskDeleteRequest: TaskDeleteRequest): Status<TaskStatus>
}