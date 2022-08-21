package ru.netology.nmedia.application

import android.app.Application
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.di.AppContainer
import timber.log.Timber

class App: Application() {

    val appContainer by lazy { createContainer() }

    override fun onCreate() {
        super.onCreate()
        createContainer()
        AppAuth.initApp(this)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun createContainer() : AppContainer {
        return AppContainer(applicationContext)
    }
}

