package com.happy.playground.repository.api.model


import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.typeadapter.EnsureLong
import com.happy.playground.util.typeadapter.ISO8610Date
import se.ansman.kotshi.JsonSerializable

data class PhotosResponse (
    val photos: List<Photo>
) : MockableResponse()

@JsonSerializable
data class Photo (
    @ISO8610Date val date_taken: Long?,
    val title: String?,
    val url: String?,
    @EnsureLong val height: Long?,
    @EnsureLong val width: Long?
)

fun Photo.toPhotoEntity(): PhotoEntity {
    return PhotoEntity(
        id = 0,
        date_taken = date_taken,
        title = title,
        url = url,
        height = height,
        width =  width
    )
}

fun List<Photo>?.toPhotoEntities(): List<PhotoEntity>? {
    return this?.map{ it.toPhotoEntity() }
}