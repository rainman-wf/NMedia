package ru.netology.nmedia.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.repository.PostRepository

class GetAllUseCase(private val repository: PostRepository) {
    operator fun invoke(): LiveData<FeedModel> {
        return repository.posts.map { list -> FeedModel(list.associateBy { it.key }.toMutableMap()) }.asLiveData(Dispatchers.Default)
    }
}