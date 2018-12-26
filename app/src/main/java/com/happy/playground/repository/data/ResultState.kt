package com.happy.playground.repository.data

sealed class ResultState
class ServerResult(val data: Any?) : ResultState()
class LocalResult(val data: Any?) : ResultState()

class ServerError() : ResultState()
class LocalError() : ResultState()
