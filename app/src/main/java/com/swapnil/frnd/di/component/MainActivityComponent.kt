package com.swapnil.frnd.di.component

import com.swapnil.frnd.MainActivity
import com.swapnil.frnd.di.module.CalendarAdapterModule
import com.swapnil.frnd.utility.adapters.OnDateChangeListener
import dagger.BindsInstance
import dagger.Component
import java.time.LocalDate

@Component(modules = [CalendarAdapterModule::class])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance selectedDate: LocalDate, @BindsInstance onDateChangeListener: OnDateChangeListener): MainActivityComponent
    }
}