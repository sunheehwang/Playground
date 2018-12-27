package com.happy.playground.photos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.happy.playground.R
import com.happy.playground.photos.adapter.PhotosAdapter
import com.happy.playground.photos.photo.ui.newIntentForPhotoActivity
import com.happy.playground.repository.Repository
import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ServerResult
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.util.Schedulers
import com.happy.playground.util.TimberLogger
import com.happy.playground.util.extensions.inflate
import com.happy.playground.util.extensions.showSnackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photos.*
import timber.log.Timber
import javax.inject.Inject

fun newPhotosFragment(): Fragment {
    return PhotosFragment().apply {

    }
}

class PhotosFragment : Fragment() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var schedulers: Schedulers

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
        getPhotos()
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter { position ->
            activity?.let {
                it.startActivity(it.newIntentForPhotoActivity(position))
            }
        }
        recycler_view.adapter = photosAdapter
    }

    @SuppressWarnings("CheckResult")
    private fun getPhotos() {
        Timber.d("getPhoto")
        repository.getPhotos()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                when (it) {
                    is ServerResult -> {
                        it.data?.let {
                            TimberLogger.debug("getPhotos serverresult ${(it as List<PhotoEntity>)}")
                            container.showSnackbar("server !!!!", Snackbar.LENGTH_INDEFINITE)
                            photosAdapter.setData(it)
                        }
                    }

                    is LocalResult -> {
                        it.data?.let {
                            TimberLogger.debug("getPhotos localresult ${(it as List<PhotoEntity>)}")
                            container.showSnackbar("local !!!!", Snackbar.LENGTH_INDEFINITE)
                            photosAdapter.setData(it)
                        }
                    }
                }
            }, {
                TimberLogger.debug("error getPhotos")
                container.showSnackbar("error offline !!!!", Snackbar.LENGTH_INDEFINITE)
            })
    }

    companion object {
        const val TAG = "PhotosFragment"
    }

}