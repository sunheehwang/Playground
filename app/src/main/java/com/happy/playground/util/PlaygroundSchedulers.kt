package com.happy.playground.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


object PlaygroundSchedulers : Schedulers {
    override fun io(): Scheduler = io.reactivex.schedulers.Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.computation()

    override fun newThread(): Scheduler = io.reactivex.schedulers.Schedulers.newThread()
}