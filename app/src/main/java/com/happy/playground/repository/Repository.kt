package com.happy.playground.repository

import com.happy.playground.repository.data.ResultState
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface Repository {
    fun getPhotos() : Flowable<ResultState>
}