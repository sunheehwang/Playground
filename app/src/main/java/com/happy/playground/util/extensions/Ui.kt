package com.happy.playground.util.extensions

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.google.android.material.snackbar.Snackbar


fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)

fun AppCompatActivity.hasFragment(layoutId: Int): Boolean = (supportFragmentManager.findFragmentById(layoutId) != null)

fun AppCompatActivity.replaceFragment(tag: String, layoutId: Int, newInstance: () -> Fragment) {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
    supportFragmentManager.transaction {
        replace(layoutId, fragment, tag)
    }
}

fun AppCompatActivity.addFragment(tag: String, layoutId: Int, newInstance: () -> Fragment) {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
    supportFragmentManager.transaction {
        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        add(layoutId, fragment, tag)
            .addToBackStack(tag)
    }

}

fun Activity.showToast(@StringRes resource: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(getString(resource), duration)

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT, isCenter: Boolean = false) {
    val toast = Toast.makeText(this, message, duration)
    if (isCenter) {
        toast.setGravity(Gravity.CENTER, 0,0)
    }
    toast.show()
}
fun Activity.showToastCenter(message: String, duration: Int = Toast.LENGTH_SHORT) =
    showToast(message, duration, true)

fun Fragment.showToast(@StringRes resource: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(getString(resource), duration)

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    activity?.showToast(message, duration)

fun Fragment.showToastCenter(message: String, duration: Int = Toast.LENGTH_SHORT) {
    activity?.showToast(message, duration, true)
}

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) =
    Snackbar.make(this, message, duration).show()


