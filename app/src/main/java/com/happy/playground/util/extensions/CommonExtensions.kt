package com.happy.playground.util.extensions

fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}