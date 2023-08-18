package com.swapnil.frnd.repository.local.database.dao

import androidx.room.*
import com.swapnil.frnd.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    suspend fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int): Int
}