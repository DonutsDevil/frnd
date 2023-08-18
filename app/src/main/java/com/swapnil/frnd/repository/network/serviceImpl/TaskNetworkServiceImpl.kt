package com.swapnil.frnd.repository.network.serviceImpl

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.repository.network.service.TaskNetworkService

class TaskNetworkServiceImpl: TaskNetworkService {

    override suspend fun getTasks(userId: Int): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun postTask(userId: Int, task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(userId: Int, taskId: Int) {
        TODO("Not yet implemented")
    }
}