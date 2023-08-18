package com.swapnil.frnd.di.module

import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.repository.local.service.TaskLocalService
import com.swapnil.frnd.repository.local.serviceImpl.TaskLocalServiceImpl
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import com.swapnil.frnd.repository.network.serviceImpl.TaskNetworkServiceImpl
import dagger.Module
import dagger.Provides

@Module
class TaskRepositoryModule {

    @Provides
    fun provideTaskNetworkService(): TaskNetworkService {
        return TaskNetworkServiceImpl()
    }

    @Provides
    fun provideTaskLocalService(taskDao: TaskDao): TaskLocalService {
        return TaskLocalServiceImpl(taskDao)
    }
}