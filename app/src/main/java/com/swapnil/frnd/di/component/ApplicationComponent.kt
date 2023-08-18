package com.swapnil.frnd.di.component

import android.content.Context
import com.swapnil.frnd.FrndApplication
import com.swapnil.frnd.di.module.RetrofitModule
import com.swapnil.frnd.di.module.TaskLocalServiceModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [TaskLocalServiceModule::class, RetrofitModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(frndApplication: FrndApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}