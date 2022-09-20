package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.data.auth.AuthState
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.models.FeedModelState
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.usecase.GetAllUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelsLiveData @Inject constructor(
    getAllUseCase: GetAllUseCase
) {

    val data: MutableLiveData<FeedModel> = getAllUseCase() as MutableLiveData<FeedModel>
    val state = MutableLiveData(FeedModelState())
    val photo = MutableLiveData<PhotoModel>(null)

    val authData: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

}