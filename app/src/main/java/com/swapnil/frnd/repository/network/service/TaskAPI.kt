package com.swapnil.frnd.repository.network.service

import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import com.swapnil.frnd.model.network.response.TaskStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskAPI {

    @POST("/api/getCalendarTaskList")
    suspend fun getAllTasks(@Body taskRequest: TaskRequest): Response<TaskResponse>?

    @POST("/api/storeCalendarTask")
    suspend fun postTask(@Body taskPostRequest: TaskPostRequest): Response<TaskStatus>?

    @POST("/api/deleteCalendarTask")
    suspend fun deleteTask(@Body taskDeleteRequest: TaskDeleteRequest): Response<TaskStatus>?
}