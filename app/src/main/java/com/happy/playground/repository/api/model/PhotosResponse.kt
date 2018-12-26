package com.happy.playground.repository.api.model


import com.happy.playground.repository.database.model.PhotoEntity

data class PhotosResponse (
    val photos: List<Photo>
) : MockableResponse()

data class Photo (
    val date_taken: String?,
    val title: String?,
    val url: String?,
    val height: String?,
    val width: String?
)

fun Photo.toPhotoEntity(id: Long): PhotoEntity {
    return PhotoEntity(
        id = id,
        date_taken = date_taken,
        title = title,
        url = url,
        height = height,
        width =  width
    )
}

fun List<Photo>?.toPhotoEntities(): List<PhotoEntity>? {
    return this?.mapIndexed{ index, photo -> photo.toPhotoEntity(index.toLong()) }
}