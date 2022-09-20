package ru.netology.nmedia.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.common.constants.APP_SETTINGS
import ru.netology.nmedia.data.local.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context) : AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "app.db").build()
    }
}