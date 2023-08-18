package com.swapnil.frnd.repository.network.service

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.network.request.TaskDeleteRequest
import com.swapnil.frnd.model.network.request.TaskPostRequest
import com.swapnil.frnd.model.network.request.TaskRequest
import com.swapnil.frnd.model.network.response.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskAPI {

    @POST("/api/public/get")
    suspend fun getAllTasks(@Body taskRequest: TaskRequest): Response<List<Task>>

    @POST("/api/storeCalendarTask")
    suspend fun postTask(@Body taskPostRequest: TaskPostRequest): Response<TaskResponse>

    @POST("/api/deleteCalendarTask")
    suspend fun deleteTask(@Body taskDeleteRequest: TaskDeleteRequest): Response<TaskResponse>
}