package com.happy.playground.repository.infrastructure

import com.happy.playground.BuildConfig
import com.happy.playground.repository.Repository
import com.happy.playground.repository.api.MockableApiService
import com.happy.playground.repository.api.model.PhotosResponse
import com.happy.playground.repository.api.model.toPhotoEntities
import com.happy.playground.repository.data.ErrorResult
import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ResultState
import com.happy.playground.repository.data.ServerResult
import com.happy.playground.repository.database.PhotoDao
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.Schedulers
import com.happy.playground.util.TimberLogger
import io.reactivex.Flowable
import io.reactivex.Notification
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlaygroundRepository @Inject constructor(
    private val mockableApiService: MockableApiService,
    private val photoDao: PhotoDao,
    private val schedulers: Schedulers
) : Repository {


    //로컬 x, 서버 x -> offline 오류
    //로컬 x, 서버 o -> serveresult
    //로컬 o, 서버 x -> localresult
    //로컬 o, 서버 o -> serveresult
    //네트워크가 로컬액세스 보다 빠를 경우
    override fun getPhotos(): Flowable<ResultState<List<PhotoEntity>?>> {
        var localResult: LocalResult<List<PhotoEntity>?>? = null
        return Flowable.concatArrayEager(
            photoDao.getPhotos().toFlowable().map {
                synchronized(this) {
                    localResult = LocalResult(it)
                }
                TimberLogger.debug("local $it")
                localResult!!
            },
            getPhotosFromApi().map {
                TimberLogger.debug("api")
                ServerResult(it.photos.toPhotoEntities())
            }
                .toFlowable()
                .materialize()
                .map {
                    if (it.error != null) {
                        TimberLogger.debug("api error")
                        synchronized(this) {
                            if (!(localResult?.data?.isNullOrEmpty() ?: false)) {
                                TimberLogger.debug("api error1")
                                Notification.createOnNext(localResult!!)
                            } else {
                                TimberLogger.debug("api error2")
                                Notification.createOnNext(
                                    ErrorResult<List<PhotoEntity>?>(
                                        null,
                                        it.error?.message ?: ""
                                    )
                                )
                            }
                        }
                    } else {
                        TimberLogger.debug("api ")
                        it
                    }
                }
                .dematerialize<ResultState<List<PhotoEntity>?>>()
                .debounce(DEBOUNCE_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
        )
    }


    override fun getPhotosFromDb(): Flowable<ResultState<List<PhotoEntity>?>> =
        photoDao.getPhotos()
            .map {
                LocalResult<List<PhotoEntity>?>(it)
            }
            .toFlowable()
            .materialize()
            .map {
                if (it.error != null) {
                    Notification.createOnNext(ErrorResult<List<PhotoEntity>?>(null,it.error?.message ?: ""))
                } else {
                    it
                }
            }
            .dematerialize()


    private fun getPhotosFromApi(): Single<PhotosResponse> {
        return mockableApiService.getPhotos()
            .doAfterSuccess {
                TimberLogger.debug("db!! setPhotos")
                photoDao.setPhotos(it.photos.toPhotoEntities())
                    .subscribeOn(schedulers.io())
                    .subscribe({
                        TimberLogger.debug("setPhoto complete")
                    }, {
                        TimberLogger.error("setPhoto error")
                    })
            }
    }

    companion object {
        var DEBOUNCE_TIMEOUT_MILLISECONDS = if (BuildConfig.DEBUG) 750L else 750L
    }
}