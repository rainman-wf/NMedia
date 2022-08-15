package ru.netology.nmedia.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.domain.repository.PostRepository

class GetNewerUseCase(private val repository: PostRepository) {

    operator fun invoke(): LiveData<Int> {
        return repository.getNewerCount().asLiveData(Dispatchers.Default)
    }
}