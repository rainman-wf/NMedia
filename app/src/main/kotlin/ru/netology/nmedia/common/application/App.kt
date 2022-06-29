package ru.netology.nmedia.common.application

import android.app.Application
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.common.di.AppContainer
import timber.log.Timber

class App: Application() {

    val appContainer by lazy { createContainer() }

    override fun onCreate() {
        super.onCreate()
        createContainer()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun createContainer() : AppContainer {
        return AppContainer(applicationContext)
    }
}

