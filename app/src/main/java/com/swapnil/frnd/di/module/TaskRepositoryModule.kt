package com.swapnil.frnd.di.module

import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.repository.local.service.TaskLocalService
import com.swapnil.frnd.repository.local.serviceImpl.TaskLocalServiceImpl
import com.swapnil.frnd.repository.network.service.TaskAPI
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import com.swapnil.frnd.repository.network.serviceImpl.TaskNetworkServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TaskRepositoryModule {

    @Provides
    fun provideTaskNetworkService(taskAPI: TaskAPI): TaskNetworkService {
        return TaskNetworkServiceImpl(taskAPI)
    }

    @Provides
    fun provideTaskLocalService(taskDao: TaskDao): TaskLocalService {
        return TaskLocalServiceImpl(taskDao)
    }

    @Provides
    fun provideTaskAPI(retrofit: Retrofit): TaskAPI {
        return retrofit.create(TaskAPI::class.java)
    }
}