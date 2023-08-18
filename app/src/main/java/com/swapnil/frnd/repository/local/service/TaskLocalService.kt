package com.swapnil.frnd.repository.local.service

import com.swapnil.frnd.model.Task

interface TaskLocalService {
    suspend fun getTasks(): List<Task>
    suspend fun updateTask(task: Task): Int
    suspend fun deleteTask(taskId: Int): Int
}