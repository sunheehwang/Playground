package com.happy.playground.repository.database

import androidx.room.*
import com.happy.playground.repository.database.model.PhotoEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos")
    fun getPhotos(): Maybe<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE id =:id")
    fun getPhoto(id: Long): Maybe<PhotoEntity>


    /*@Transaction
    fun setPhotos(photoEntity: List<PhotoEntity>?) {
        deletePhotos()
        photoEntity?.forEach {
            insertPhoto(it)
        }

    }*/

    @Transaction
    fun setPhotos(photoEntity: List<PhotoEntity>?): Completable {
        return Completable.fromAction {
            deletePhotos()
            photoEntity?.forEach {
                insertPhoto(it)
            }
        }
    }

    @Query("DELETE FROM photos")
    fun deletePhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photoEntity: PhotoEntity)

}