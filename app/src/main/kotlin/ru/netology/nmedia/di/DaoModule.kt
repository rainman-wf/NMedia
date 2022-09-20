package ru.netology.nmedia.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.data.local.dao.PostDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    @Singleton
    fun providePostDao(db: AppDb) : PostDao = db.postDao

}