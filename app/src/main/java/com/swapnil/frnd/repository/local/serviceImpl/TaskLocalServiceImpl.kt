package com.swapnil.frnd.repository.local.serviceImpl

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.repository.local.service.TaskLocalService

class TaskLocalServiceImpl: TaskLocalService {

    override suspend fun getTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: Int): Int {
        TODO("Not yet implemented")
    }
}