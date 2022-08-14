package ru.netology.nmedia.di

import android.content.Context
import ru.netology.nmedia.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.common.constants.BASE_URL
import ru.netology.nmedia.data.PostsRepositoryImpl
import ru.netology.nmedia.data.api.ApiServiceHolder
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.domain.usecase.*
import ru.netology.nmedia.domain.usecase.container.NewPostUseCaseContainer
import ru.netology.nmedia.domain.usecase.container.PostDetailsUseCaseContainer
import ru.netology.nmedia.domain.usecase.container.PostListUseCaseContainer
import ru.netology.nmedia.presentation.viewmodels.*
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val dbInstance = AppDb.getInstance(context)

    private val postDao = dbInstance.postDao

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    private val apiServiceHolder = ApiServiceHolder(BASE_URL, okHttpClient)


    private val postRepository = PostsRepositoryImpl(apiServiceHolder.api, postDao)

    private val getAllUseCase = GetAllUseCase(postRepository)
    private val savePostUseCase = SavePostUseCase(postRepository)
    private val trySendPostUseCase = TrySendPostUseCase(postRepository)
    private val likePostUseCase = LikePostUseCase(postRepository)
    private val removePostUseCase = RemovePostUseCase(postRepository)
    private val syncDataUseCase = SyncDataUseCase(postRepository)
    private val getPostByIdUseCase = GetPostByIdUseCase(postRepository)

    private val newPostUseCaseContainer = NewPostUseCaseContainer(
        trySendPostUseCase,
        savePostUseCase,
        removePostUseCase
    )

    private val postListUseCaseContainer = PostListUseCaseContainer(
        getAllUseCase,
        trySendPostUseCase,
        likePostUseCase,
        removePostUseCase,
        syncDataUseCase
    )

    private val postDetailsUseCaseContainer = PostDetailsUseCaseContainer(
        likePostUseCase,
        removePostUseCase,
        getPostByIdUseCase
    )

    private val postsLiveData = LiveData(getAllUseCase)

    val newPostViewModelFactory = NewPostViewModelFactory(newPostUseCaseContainer)
    val detailsViewModelFactory =
        DetailsViewModelFactory(postsLiveData, postDetailsUseCaseContainer)
    val postListViewModelFactory = PostListViewModelFactory(postsLiveData, postListUseCaseContainer)
}




