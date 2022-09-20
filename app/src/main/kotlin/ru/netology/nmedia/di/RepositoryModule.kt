package ru.netology.nmedia.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.data.AuthManagerImpl
import ru.netology.nmedia.data.PostRepositoryImpl
import ru.netology.nmedia.domain.repository.AuthManager
import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindPostRepository(impl: PostRepositoryImpl) : PostRepository

    @Binds
    @Singleton
    fun bindAuthService(impl: AuthManagerImpl) : AuthManager

}