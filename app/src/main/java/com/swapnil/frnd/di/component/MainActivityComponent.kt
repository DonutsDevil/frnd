package com.swapnil.frnd.di.component

import android.content.Context
import com.swapnil.frnd.MainActivity
import com.swapnil.frnd.di.module.CalendarAdapterModule
import com.swapnil.frnd.di.module.TaskRepositoryModule
import com.swapnil.frnd.repository.local.database.dao.TaskDao
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import com.swapnil.frnd.utility.adapters.TaskItemAdapter
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [CalendarAdapterModule::class, TaskRepositoryModule::class])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance dialogListener: TaskItemAdapter.CalendarEventsClickListener,
            @BindsInstance context: Context,
            @BindsInstance onDateChangeListener: OnDateChangeListener,
            @BindsInstance taskDao: TaskDao,
            @BindsInstance retrofit: Retrofit
        ): MainActivityComponent
    }
}