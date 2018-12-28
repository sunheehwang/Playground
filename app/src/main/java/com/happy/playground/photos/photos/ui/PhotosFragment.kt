package com.happy.playground.photos.photos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.happy.playground.R
import com.happy.playground.photos.photos.adapter.PhotosAdapter
import com.happy.playground.photos.photo.ui.newIntentForPhotoActivity
import com.happy.playground.photos.photos.viewmodel.PhotosViewModel
import com.happy.playground.repository.api.model.Photo
import com.happy.playground.repository.data.ErrorResult
import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ServerResult
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.TimberLogger
import com.happy.playground.util.extensions.inflate
import com.happy.playground.util.extensions.showSnackbar
import com.squareup.moshi.Moshi
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photos.*
import javax.inject.Inject
import com.squareup.moshi.JsonAdapter



fun newPhotosFragment(): Fragment {
    return PhotosFragment().apply {

    }
}

class PhotosFragment : Fragment() {

    @Inject
    lateinit var viewModel: PhotosViewModel

    @Inject
    lateinit var moshi: Moshi

    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_photos)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.photosLiveData.observe(this, Observer {
            when (it) {
                is ServerResult -> {
                    it.data?.let {
                        TimberLogger.debug("getPhotos serverresult ${(it as List<PhotoEntity>)}")
                        container.showSnackbar("**server !!!!", Snackbar.LENGTH_INDEFINITE)
                        photosAdapter.setData(it)
                    }
                }

                is LocalResult -> {
                    it.data?.let {
                        TimberLogger.debug("getPhotos localresult ${(it as List<PhotoEntity>)}")
                        container.showSnackbar("**local !!!!", Snackbar.LENGTH_INDEFINITE)
                        photosAdapter.setData(it)
                    }
                }
                is ErrorResult -> {
                        TimberLogger.debug("getPhotos errorresult ${(it)}")
                        container.showSnackbar("**error !!!! ${it.message}", Snackbar.LENGTH_INDEFINITE)
                }
            }
        })


        //"2015-04-26 23:04:07"
       /* val photo = Photo(1430057047000, "this is title", "http://www.daum.net", 1234, 567 )
        val jsonAdapter = moshi.adapter<Photo>(Photo::class.java)
        val json = jsonAdapter.toJson(photo)
        TimberLogger.debug("json=$json")*/
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter { position ->
            activity?.let {
                it.startActivity(it.newIntentForPhotoActivity(position))
            }
        }
        recycler_view.adapter = photosAdapter
    }

    companion object {
        const val TAG = "PhotosFragment"
    }

}