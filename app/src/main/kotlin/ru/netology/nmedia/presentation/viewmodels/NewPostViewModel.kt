package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.usecase.AddNewPostUseCase
import ru.netology.nmedia.domain.usecase.GetPostByIdUseCase
import ru.netology.nmedia.domain.usecase.UpdatePostContentUseCase
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent
import kotlin.concurrent.thread

class NewPostViewModel(
    private val updatePostContentUseCase: UpdatePostContentUseCase,
    private val addNewPostUseCase: AddNewPostUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase
) : ViewModel() {

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun save(author: String, content: String) {
        val newPostParam = NewPost(author, content)
        thread {
            try {
                addNewPostUseCase.invoke(newPostParam)
                _postCreated.postValue(Unit)
            } catch (exception: Exception) {
                TODO()
            }

        }
    }

    fun save(id: Long, content: String) {
        val updatePostContent = UpdatePostContent(id, content)
        thread {
            try {
                updatePostContentUseCase.invoke(updatePostContent)
                _postCreated.postValue(Unit)
            } catch (exception: Exception) {
                TODO()
            }
        }
    }
}