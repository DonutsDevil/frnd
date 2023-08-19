package com.swapnil.frnd.repository.local.serviceImpl

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.repository.local.service.TaskLocalService
import javax.inject.Inject

class TaskLocalServiceImpl @Inject constructor(private val taskDao: TaskDao) : TaskLocalService {

    override suspend fun getTasks(): List<Task> {
        return taskDao.getTasks()
    }

    override suspend fun deleteTask(taskId: Int): Int {
        return taskDao.deleteTask(taskId)
    }

    override suspend fun addTasks(tasks: List<Task>) {
        taskDao.insertTasks(tasks)
    }
}