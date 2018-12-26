package com.happy.playground.repository.infrastructure

import com.happy.playground.BuildConfig
import com.happy.playground.repository.Repository
import com.happy.playground.repository.api.MockableApiService
import com.happy.playground.repository.api.model.PhotosResponse
import com.happy.playground.repository.api.model.toPhotoEntities
import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ResultState
import com.happy.playground.repository.data.ServerResult
import com.happy.playground.repository.database.PhotoDao
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.TimberLogger
import io.reactivex.Flowable
import io.reactivex.Notification
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PlaygroundRepository constructor(
    private val mockableApiService: MockableApiService,
    private val photoDao: PhotoDao
) : Repository {


    //로컬 x, 서버 x -> offline 오류
    //로컬 x, 서버 o -> serveresult
    //로컬 o, 서버 x -> localresult
    //로컬 o, 서버 o -> serveresult
    //네트워크가 로컬액세스 보다 빠를 경우
    override fun getPhotos(): Flowable<ResultState> {
        var localResult: LocalResult? = null
        return Flowable.concatArray(
            photoDao.getPhotos().toFlowable().map {
                synchronized(this) {
                    localResult = LocalResult(it)
                }
                TimberLogger.debug("local $it")
                localResult!!
            }
                .subscribeOn(Schedulers.io()),
            getPhotosFromApi().map {
                TimberLogger.debug("api")
                ServerResult(it.photos.toPhotoEntities()) }
                .toFlowable()
                .materialize()
                .map {
                    if (it.error != null) {
                        TimberLogger.debug("api error")
                        synchronized(this) {
                            if (localResult != null && (localResult?.data as List<PhotoEntity>).isNotEmpty()){
                                TimberLogger.debug("api error1")
                                Notification.createOnNext(localResult!!)
                            }
                            else {
                                TimberLogger.debug("api error2")
                                it
                            }
                        }
                    } else {
                        TimberLogger.debug("api ")
                        it
                    }
                }
                .dematerialize<ResultState>()
                .subscribeOn(Schedulers.io())
        ).debounce(DEBOUNCE_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
    }

    private fun getPhotosFromApi(): Single<PhotosResponse> {
        return mockableApiService.getPhotos()
            .doAfterSuccess {
                TimberLogger.debug("db!! setPhotos")
                photoDao.setPhotos(it.photos.toPhotoEntities())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        TimberLogger.debug("setPhoto complete")
                    }, {
                        TimberLogger.error("setPhoto error")
                    })
            }
    }

    companion object {
        var DEBOUNCE_TIMEOUT_MILLISECONDS = if (BuildConfig.DEBUG) 750L else 550L
    }
}