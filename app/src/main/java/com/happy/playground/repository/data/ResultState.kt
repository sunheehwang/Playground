package com.happy.playground.repository.data

sealed class ResultState<out T>(val data: T)
class ServerResult<T>(data: T) : ResultState<T>(data)
class LocalResult<T>(data: T) : ResultState<T>(data)
