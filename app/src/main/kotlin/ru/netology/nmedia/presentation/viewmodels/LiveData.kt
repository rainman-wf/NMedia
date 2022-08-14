package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.models.FeedModelState
import ru.netology.nmedia.domain.usecase.GetAllUseCase

class LiveData(getAllUseCase: GetAllUseCase) {

    val data: MutableLiveData<FeedModel> = getAllUseCase() as MutableLiveData<FeedModel>
    val state = MutableLiveData(FeedModelState())

}