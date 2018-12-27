package com.happy.playground.photos.photo.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happy.playground.R
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.extensions.ifNull
import com.happy.playground.util.extensions.inflate
import com.happy.playground.util.glide.loadImage
import kotlinx.android.synthetic.main.item_photo_detail.view.*

class PhotoDetailsAdapter: RecyclerView.Adapter<PhotoDetailsAdapter.ViewHolder> () {

    private val dataSet = mutableListOf<PhotoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  = ViewHolder(parent.inflate(R.layout.item_photo_detail))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position, dataSet[position])

    override fun getItemCount(): Int = dataSet.size

    fun setData(data: List<PhotoEntity>?) {
        dataSet.clear()
        data?.let {
            dataSet.addAll(data)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, photoEntity: PhotoEntity) {
            with(itemView) {
                photoEntity.url?.let {
                    image_view.visibility = View.VISIBLE
                    image_view.loadImage(it, 0.5f)
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