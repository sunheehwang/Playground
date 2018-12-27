package com.happy.playground.photos.photo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.happy.playground.R
import com.happy.playground.util.extensions.replaceFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

fun Context.newIntentForPhotoActivity(position: Int): Intent {
    val intent = Intent(this, PhotoActivity::class.java).apply {
        putExtra(INTENT_PHOTO_POSITION, position)
    }
    return intent
}

fun FragmentActivity.newIntentForPhotoActivity(position: Int): Intent {
    return (this as Context).newIntentForPhotoActivity(position)
}

internal const val INTENT_PHOTO_POSITION = "intent_photo_position"
class PhotoActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        setupIntent()
        replaceFragment(PhotoFragment.TAG, R.id.fragment_container) {
            newPhotoFragment(position)
        }
    }

    private fun setupIntent() {
        position = intent.getIntExtra(INTENT_PHOTO_POSITION, -1)
    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = fragmentDispatchingAndroidInjector
}