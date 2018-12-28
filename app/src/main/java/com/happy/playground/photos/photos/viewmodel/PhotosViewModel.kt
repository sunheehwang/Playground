package com.happy.playground.photos.photos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.happy.playground.repository.Repository
import com.happy.playground.repository.data.ResultState
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.Schedulers
import javax.inject.Inject

class PhotosViewModel @Inject constructor(private val repository: Repository, private val schedulers: Schedulers)  : ViewModel() {

    val photosLiveData: LiveData<ResultState<List<PhotoEntity>?>> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            repository.getPhotos()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
        )
    }
}