package com.swapnil.frnd.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.repository.local.database.dao.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class FrndDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
}