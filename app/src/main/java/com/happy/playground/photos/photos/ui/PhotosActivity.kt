package com.happy.playground.photos.photos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.happy.playground.R
import com.happy.playground.util.extensions.replaceFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PhotosActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        replaceFragment(PhotosFragment.TAG, R.id.fragment_container) {
            newPhotosFragment()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = fragmentDispatchingAndroidInjector

}