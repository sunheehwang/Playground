package com.happy.playground.photos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.happy.playground.R
import com.happy.playground.photos.adapter.PhotosAdapter
import com.happy.playground.repository.api.model.Photo
import com.happy.playground.repository.data.LocalResult
import com.happy.playground.repository.data.ServerResult
import com.happy.playground.repository.database.model.PhotoEntity
import com.happy.playground.repository.infrastructure.PlaygroundRepository
import com.happy.playground.util.TimberLogger
import com.happy.playground.util.extensions.inflate
import com.happy.playground.util.extensions.showSnackbar
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photos.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

fun newPhotosFragment(): Fragment {
    return PhotosFragment()
}

class PhotosFragment : Fragment() {

    @Inject
    lateinit var repository: PlaygroundRepository

    private lateinit var photosAdapter : PhotosAdapter

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
        photosAdapter = PhotosAdapter{

        }
        recycler_view.adapter = photosAdapter
    }

    @SuppressWarnings("CheckResult")
    private fun getPhotos() {
        Timber.d("getPhoto")
        repository.getPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                            container.showSnackbar("local !!!!",Snackbar.LENGTH_INDEFINITE)
                            photosAdapter.setData(it)
                        }
                    }
                }
            }, {
                TimberLogger.debug("error getPhotos")
                container.showSnackbar("error offline !!!!",Snackbar.LENGTH_INDEFINITE)
            })
    }

    companion object {
        const val TAG = "PhotosFragment"
    }

}