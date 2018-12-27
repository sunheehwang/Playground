package com.happy.playground.photos.photo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.happy.playground.R
import com.happy.playground.photos.photo.adapter.PhotoDetailsAdapter
import com.happy.playground.repository.Repository
import com.happy.playground.ui.widget.CirclePagerIndicatorDecoration
import com.happy.playground.util.Schedulers
import com.happy.playground.util.TimberLogger
import com.happy.playground.util.extensions.inflate
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject


fun newPhotoFragment(position: Int): Fragment {
    return PhotoFragment().apply {
        arguments = Bundle(1).apply {
            putInt(BUNDLE_PHOTO_POSITION, position)
        }
    }
}

internal const val BUNDLE_PHOTO_POSITION = "bundle_photo_position"

class PhotoFragment : Fragment() {

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var schedulers: Schedulers

    private lateinit var photoDetailsAdapter: PhotoDetailsAdapter
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            position = bundle.getInt(BUNDLE_PHOTO_POSITION, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_photo)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getPhotosFromDb()
    }

    private fun setupRecyclerView() {
        photoDetailsAdapter = PhotoDetailsAdapter()
        recycler_view.adapter = photoDetailsAdapter
        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.addItemDecoration(CirclePagerIndicatorDecoration())
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view)
    }

    @SuppressWarnings("CheckResult")
    private fun getPhotosFromDb() {
        repository.getPhotosFromDb()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                TimberLogger.debug("getPhotosFromDb $it")
                photoDetailsAdapter.setData(it.data)
                recycler_view.scrollToPosition(position)
            }, {
                TimberLogger.error("error getPhotosFromDb $it")
            })
    }

    companion object {
        const val TAG = "PhotoFragment"
    }
}