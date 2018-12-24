package com.happy.playground.repository.database

import androidx.room.*
import com.happy.playground.repository.database.model.PhotoEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
abstract class PhotoDao {

    @Query("SELECT * FROM photos")
    abstract fun getPhotos(): Maybe<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE id =:id")
    abstract fun getPhoto(id: Long): Maybe<PhotoEntity>

    @Transaction
    open fun setPhotos(photoEntity: List<PhotoEntity>?): Completable {
        return Completable.fromAction {
            deletePhotos()
            photoEntity?.forEach {
                insertPhoto(it)
            }
        }
    }

    @Query("DELETE FROM photos")
    abstract fun deletePhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhoto(photoEntity: PhotoEntity)

}