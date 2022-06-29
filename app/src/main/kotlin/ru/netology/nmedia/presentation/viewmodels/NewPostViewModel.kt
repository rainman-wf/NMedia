package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.usecase.AddNewPostUseCase
import ru.netology.nmedia.domain.usecase.GetPostByIdUseCase
import ru.netology.nmedia.domain.usecase.UpdatePostContentUseCase
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

class NewPostViewModel(
    private val updatePostContentUseCase: UpdatePostContentUseCase,
    private val addNewPostUseCase: AddNewPostUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase
) : ViewModel() {

    fun getPost(id: Long) = getPostByIdUseCase.invoke(id)

    fun onSaveClicked (author : String, content: String, url: String? = null) {
        val newPost = NewPost(author, content, url)
        addNewPostUseCase.invoke(newPost)
    }

    fun onSaveClicked(id: Long, content: String, url: String?) {
        val updatePostContent = UpdatePostContent(id, content, url)
        updatePostContentUseCase.invoke(updatePostContent)
    }
}