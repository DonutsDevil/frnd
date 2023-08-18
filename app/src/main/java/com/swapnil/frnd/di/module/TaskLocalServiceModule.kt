package com.swapnil.frnd.di.module

import android.content.Context
import androidx.room.Room
import com.swapnil.frnd.repository.local.database.FrndDatabase
import com.swapnil.frnd.repository.local.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TaskLocalServiceModule {

    @Singleton
    @Provides
    fun providesFrndDB(context: Context): FrndDatabase {
        return Room.databaseBuilder(context.applicationContext, FrndDatabase::class.java, "FrndDatabase").build()
    }

    @Provides
    fun provideClipBoardItemDao(db: FrndDatabase): TaskDao {
        return db.getTaskDao()
    }
}