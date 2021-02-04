package com.example.android.politicalpreparedness

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
        Timber.plant(Timber.DebugTree())

    }

}