package com.happy.playground.repository

import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ResultState
import com.happy.playground.repository.database.model.PhotoEntity
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface Repository {
    fun getPhotos() : Flowable<ResultState<List<PhotoEntity>?>>
    fun getPhotosFromDb() : Flowable<LocalResult<List<PhotoEntity>?>>
}