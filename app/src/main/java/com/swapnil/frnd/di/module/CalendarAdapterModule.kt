package com.swapnil.frnd.di.module

import com.swapnil.frnd.model.Task
import com.swapnil.frnd.utility.adapters.CalendarAdapter
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import com.swapnil.frnd.utility.adapters.TaskItemAdapter
import dagger.Module
import dagger.Provides
import java.time.LocalDate

@Module
class CalendarAdapterModule {
    @Provides
    fun provideCalendarAdapter(onDateChangeListener: OnDateChangeListener): CalendarAdapter {
        return CalendarAdapter(LocalDate.now(), onDateChangeListener, listOf<LocalDate>())
    }

    @Provides
    fun providerTaskItemAdapter(dialogListener: TaskItemAdapter.CalendarEventsClickListener): TaskItemAdapter {
        return TaskItemAdapter(dialogListener, listOf<Task>())
    }
}