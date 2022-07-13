package ru.netology.nmedia.di

import android.content.Context
import okhttp3.OkHttpClient
import ru.netology.nmedia.data.PostsRepositoryImpl
import ru.netology.nmedia.data.UnsentPostRepositoryImpl
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.data.remote.RemoteDataSource
import ru.netology.nmedia.domain.usecase.*
import ru.netology.nmedia.domain.usecase.interactor.NewPostInteractor
import ru.netology.nmedia.domain.usecase.interactor.PostListInteractor
import ru.netology.nmedia.presentation.viewmodels.*
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val dbInstance = AppDb.getInstance(context)
    private val okHttpClient = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build()

    private val postDao = dbInstance.postDao
    private val unsentPostDao = dbInstance.unsentPostDao

    private val remoteDataSource = RemoteDataSource(okHttpClient)

    private val unsentPostsRepository = UnsentPostRepositoryImpl(unsentPostDao)
    private val postRepository = PostsRepositoryImpl(remoteDataSource, postDao)

    private val postsLiveData = PostsLiveData()

    private val getAllUseCase = GetAllUseCase(postRepository)
    private val getAllWorkpiecesUseCase = GetAllWorkpiecesUseCase(unsentPostsRepository)
    private val savePostUseCase = SavePostUseCase(postRepository, unsentPostsRepository)
    private val sendPostUseCase = SendPostUseCase(postRepository)
    private val likePostUseCase = LikePostUseCase(postRepository)
    private val removePostUseCase = RemovePostUseCase(postRepository, unsentPostsRepository)


    private val newPostInteractor = NewPostInteractor(
        sendPostUseCase,
        savePostUseCase,
        removePostUseCase
    )

    private val postListInteractor = PostListInteractor(
        getAllWorkpiecesUseCase,
        getAllUseCase,
        sendPostUseCase,
        likePostUseCase,
        removePostUseCase
    )

    val newPostViewModelFactory = NewPostViewModelFactory(postsLiveData, newPostInteractor)
    val detailsViewModelFactory = DetailsViewModelFactory(postRepository)
    val postListViewModelFactory = PostListViewModelFactory(postsLiveData, postListInteractor)
}




