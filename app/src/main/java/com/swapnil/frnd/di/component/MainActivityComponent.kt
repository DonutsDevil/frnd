package com.swapnil.frnd.di.component

import com.swapnil.frnd.MainActivity
import com.swapnil.frnd.di.module.CalendarAdapterModule
import com.swapnil.frnd.di.module.TaskRepositoryModule
import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import dagger.BindsInstance
import dagger.Component

@Component(modules = [CalendarAdapterModule::class, TaskRepositoryModule::class])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance onDateChangeListener: OnDateChangeListener, @BindsInstance taskDao: TaskDao): MainActivityComponent
    }
}