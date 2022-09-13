package ru.netology.nmedia.di

import android.content.Context
import android.content.SharedPreferences
import ru.netology.nmedia.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.common.constants.APP_SETTINGS
import ru.netology.nmedia.common.constants.BASE_URL
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.AuthServiceImpl
import ru.netology.nmedia.data.PostsRepositoryImpl
import ru.netology.nmedia.data.api.ApiServiceHolder
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.domain.repository.AuthService
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
        .addInterceptor { chain ->
            AppAuth.getInstance().authStateFlow.value.token?.let { token ->
                log("token : $token")
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            chain.proceed(chain.request())
        }
        .build()

    private val apiServiceHolder = ApiServiceHolder(BASE_URL, okHttpClient)


    private val postRepository = PostsRepositoryImpl(apiServiceHolder.api, postDao)

    private val authService = AuthServiceImpl(context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE), apiServiceHolder.api)

    private val getAllUseCase = GetAllUseCase(postRepository)
    private val savePostUseCase = SavePostUseCase(postRepository)
    private val trySendPostUseCase = TrySendPostUseCase(postRepository)
    private val likePostUseCase = LikePostUseCase(postRepository)
    private val removePostUseCase = RemovePostUseCase(postRepository)
    private val syncDataUseCase = SyncDataUseCase(postRepository)
    private val getPostByIdUseCase = GetPostByIdUseCase(postRepository)
    private val getNewerUseCase = GetNewerUseCase(postRepository)
    private val setReadUseCase = SetReadUseCase(postRepository)

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
        syncDataUseCase,
        getNewerUseCase,
        setReadUseCase
    )

    private val postDetailsUseCaseContainer = PostDetailsUseCaseContainer(
        likePostUseCase,
        removePostUseCase,
        getPostByIdUseCase
    )

    private val postsModelsLiveData = ModelsLiveData(getAllUseCase)

    val newPostViewModelFactory = NewPostViewModelFactory(newPostUseCaseContainer, postsModelsLiveData)
    val detailsViewModelFactory =
        DetailsViewModelFactory(postsModelsLiveData, postDetailsUseCaseContainer)
    val postListViewModelFactory = PostListViewModelFactory(postsModelsLiveData, postListUseCaseContainer)
    val authViewModelFactory = AuthViewModelFactory(postsModelsLiveData, authService)
}




