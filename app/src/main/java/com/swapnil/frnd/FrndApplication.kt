package com.swapnil.frnd

import android.app.Application
import com.swapnil.frnd.di.component.DaggerApplicationComponent
import com.swapnil.frnd.repository.local.database.FrndDatabase
import javax.inject.Inject

class FrndApplication: Application() {
    @Inject
    lateinit var db: FrndDatabase

    override fun onCreate() {
        super.onCreate()
        val applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
    }
}