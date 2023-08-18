package com.swapnil.frnd.repository.network.service

import com.swapnil.frnd.model.Task

interface TaskNetworkService {
    suspend fun getTasks(userId: Int): List<Task>
    suspend fun postTask(userId: Int, task: Task)
    suspend fun deleteTask(userId: Int, taskId: Int)
}