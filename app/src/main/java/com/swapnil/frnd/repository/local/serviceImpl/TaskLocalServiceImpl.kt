package com.swapnil.frnd.repository.local.serviceImpl

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.repository.local.service.TaskLocalService
import javax.inject.Inject

class TaskLocalServiceImpl @Inject constructor(private val taskDao: TaskDao) : TaskLocalService {

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