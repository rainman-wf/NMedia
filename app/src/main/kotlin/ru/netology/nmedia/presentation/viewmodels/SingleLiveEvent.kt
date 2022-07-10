package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.netology.nmedia.common.utils.log

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private var pending = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        require(!hasActiveObservers()) {
            log("some error message")
        }

        super.observe(owner) {
            if (pending) {
                pending = false
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(value: T) {
        pending = true
        super.setValue(value)
    }
}