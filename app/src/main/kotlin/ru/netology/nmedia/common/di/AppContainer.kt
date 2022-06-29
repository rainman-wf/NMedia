package ru.netology.nmedia.common.di

import android.content.Context
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.db.AppDb
import ru.netology.nmedia.data.repository.PostRepositoryImpl
import ru.netology.nmedia.domain.usecase.*
import ru.netology.nmedia.presentation.viewmodels.*

class AppContainer(context: Context) {

    private val postDao = AppDb.getInstance(context).postDao
    private val postRepository = PostRepositoryImpl(postDao)
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
        likePostUseCase, sharePostUseCase,
        removePostUseCase, getObservableByIdUseCase
    )
    val postListViewModelFactory = PostListViewModelFactory(
        likePostUseCase, sharePostUseCase,
        removePostUseCase, getPostsListUseCase
    )
}




