package ru.netology.nmedia.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.data.auth.AppAuth
import timber.log.Timber

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AppAuth.initApp(this)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}

