package com.happy.playground.repository.api

import com.happy.playground.repository.api.model.PhotosResponse
import io.reactivex.Single
import retrofit2.http.GET

interface MockableApiService {
    @GET("images")
    fun getPhotos() : Single<PhotosResponse>
    companion object {
        const val END_POINT = "http://demo2587971.mockable.io/"
    }
}