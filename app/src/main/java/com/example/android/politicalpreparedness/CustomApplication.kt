package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.facebook.stetho.Stetho
import timber.log.Timber

class CustomApplication: Application() {

    val electionRepository :ElectionRepository
    get() =ServiceLocator.provideElectionRepository(this)

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())

    }

}