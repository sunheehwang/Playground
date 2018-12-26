package com.happy.playground.util

interface PlatformLogger {
    fun debug(s: String): Unit
    fun info(s: String): Unit
    fun warn(s: String): Unit
    fun error(s: String): Unit
    class NoOpLogger constructor() : PlatformLogger {
        override fun debug(s: String) {
        }

        override fun info(s: String) {
        }

        override fun warn(s: String) {
        }

        override fun error(s: String) {
        }

    }
}