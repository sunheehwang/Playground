package com.happy.playground.repository.api.model

open class MockableResponse(
    val stat: String? = null,
    val page: Int? = null,
    val total_page: Int? = null
) {
    override fun toString(): String {
        return "stat = ${stat}, page = ${page}, total_page = ${total_page}"
    }
}