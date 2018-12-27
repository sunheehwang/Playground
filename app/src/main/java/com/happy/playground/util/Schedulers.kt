package com.happy.playground.util

import io.reactivex.Scheduler


interface Schedulers {

    fun io() : Scheduler

    fun mainThread(): Scheduler

    fun computation(): Scheduler

    fun newThread(): Scheduler
}