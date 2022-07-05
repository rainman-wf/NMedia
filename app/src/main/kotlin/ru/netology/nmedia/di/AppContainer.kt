package ru.netology.nmedia.di

import android.content.Context
import okhttp3.OkHttpClient
import ru.netology.nmedia.data.db.AppDb
import ru.netology.nmedia.data.network.NetworkServiceImpl
import ru.netology.nmedia.data.repository.PostsRemoteDataRepositoryImpl
import ru.netology.nmedia.domain.usecase.*
import ru.netology.nmedia.presentation.viewmodels.*
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val okHttpClient = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build()

    private val postDao = AppDb.getInstance(context).postDao
//    private val postRepository = PostsLocalDataRepositoryImpl(postDao)

    private val networkService = NetworkServiceImpl(okHttpClient)
    private val postRepository = PostsRemoteDataRepositoryImpl(networkService)

    val likePostUseCase = LikePostUseCase(postRepository)
    private val sharePostUseCase = SharePostUseCase(postRepository)
    private val removePostUseCase = RemovePostUseCase(postRepository)
    private val getObservableByIdUseCase = GetObservableByIdUseCase(postRepository)
    private val updatePostContentUseCase = UpdatePostContentUseCase(postRepository)
    private val getPostByIdUseCase = GetPostByIdUseCase(postRepository)
    private val getPostsListUseCase = GetPostsListUseCase(postRepository)
    private val addNewPostUseCase = AddNewPostUseCase(postRepository)

    val addIncomingPostUseCase = AddIncomingPostUseCase(postRepository)

    val newPostViewModelFactory = NewPostViewModelFactory(
        updatePostContentUseCase, addNewPostUseCase, getPostByIdUseCase
    )
    val detailsViewModelFactory = DetailsViewModelFactory(
        likePostUseCase, sharePostUseCase, removePostUseCase,
        getPostByIdUseCase, getObservableByIdUseCase
    )
    val postListViewModelFactory = PostListViewModelFactory(
        likePostUseCase, sharePostUseCase,
        removePostUseCase, getPostsListUseCase
    )
}




