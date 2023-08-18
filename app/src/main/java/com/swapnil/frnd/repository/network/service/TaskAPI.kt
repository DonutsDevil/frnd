package com.swapnil.frnd.repository.network.service

import com.swapnil.frnd.model.Task
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Part

interface TaskAPI {

    @POST("/api/public/get")
    suspend fun getAllTasks(@Part("user_id") userId: RequestBody): Response<List<Task>>
}