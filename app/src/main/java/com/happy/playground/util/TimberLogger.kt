package com.happy.playground.util

import timber.log.Timber

object TimberLogger : PlatformLogger {
    override fun debug(s: String) {
        val t = Thread.currentThread()
        val l = t.id
        val name = t.name

        Timber.d("thread!! $name($l) $s")
    }

    override fun info(s: String) {
        val t = Thread.currentThread()
        val l = t.id
        val name = t.name

        Timber.i("thread!! $name($l) $s")
    }

    override fun warn(s: String) {
        val t = Thread.currentThread()
        val l = t.id
        val name = t.name

        Timber.w("thread!! $name($l) $s")
    }

    override fun error(s: String) {
        val t = Thread.currentThread()
        val l = t.id
        val name = t.name

        Timber.e("thread!! $name($l) $s")
    }
}