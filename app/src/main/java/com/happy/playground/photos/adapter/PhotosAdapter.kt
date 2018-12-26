package com.happy.playground.photos.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happy.playground.R
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.extensions.ifNull
import com.happy.playground.util.extensions.inflate
import com.happy.playground.util.glide.loadImage
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotosAdapter(private val listener : (Int) -> Unit): RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val photoEntities: MutableList<PhotoEntity> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.item_photo))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position, photoEntities[position])

    override fun getItemCount(): Int = photoEntities.size

    fun setData(data: List<PhotoEntity>?) {
        photoEntities.clear()
        data?.let {
            photoEntities.addAll(it)
        }
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int, photoEntity: PhotoEntity) {
            with(itemView) {
                photoEntity.url?.let {
                    image_view.visibility = View.VISIBLE
                    image_view.loadImage(it)
                }.ifNull {
                    image_view.visibility = View.GONE
                }

                text_title.text = photoEntity.title
                text_photo_size.text = listOf(photoEntity.width, photoEntity.height).joinToString ("x")
                text_photo_url.text = photoEntity.url
                text_date.text = photoEntity.date_taken
            }
        }
    }
}