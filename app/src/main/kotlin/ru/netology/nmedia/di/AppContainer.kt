package ru.netology.nmedia.di

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.common.constants.BASE_URL
import ru.netology.nmedia.data.PostsRepositoryImpl
import ru.netology.nmedia.data.UnsentPostRepositoryImpl
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
    private val unsentPostDao = dbInstance.unsentPostDao
    private val removedIdsDao = dbInstance.removedIdsDao

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    private val apiServiceHolder = ApiServiceHolder(BASE_URL, okHttpClient)

    private val unsentPostsRepository = UnsentPostRepositoryImpl(unsentPostDao)
    private val postRepository = PostsRepositoryImpl(apiServiceHolder.api, postDao, removedIdsDao)

    private val getAllUseCase = GetAllUseCase(postRepository, unsentPostsRepository)
    private val savePostUseCase = SavePostUseCase(postRepository, unsentPostsRepository)
    private val sendPostUseCase = SendPostUseCase(postRepository)
    private val likePostUseCase = LikePostUseCase(postRepository)
    private val removePostUseCase = RemovePostUseCase(postRepository, unsentPostsRepository)
    private val syncDataUseCase = SyncDataUseCase(postRepository)
    private val getPostByIdUseCase = GetPostByIdUseCase(postRepository, unsentPostsRepository)

    private val newPostUseCaseContainer = NewPostUseCaseContainer(
        sendPostUseCase,
        savePostUseCase,
        removePostUseCase
    )

    private val postListUseCaseContainer = PostListUseCaseContainer(
        getAllUseCase,
        sendPostUseCase,
        likePostUseCase,
        removePostUseCase,
        syncDataUseCase
    )

    private val postDetailsUseCaseContainer = PostDetailsUseCaseContainer(
        likePostUseCase,
        removePostUseCase,
        getPostByIdUseCase
    )

    private val postsLiveData = PostsLiveData()

    val newPostViewModelFactory = NewPostViewModelFactory(postsLiveData, newPostUseCaseContainer)
    val detailsViewModelFactory =
        DetailsViewModelFactory(postsLiveData, postDetailsUseCaseContainer)
    val postListViewModelFactory = PostListViewModelFactory(postsLiveData, postListUseCaseContainer)
}




