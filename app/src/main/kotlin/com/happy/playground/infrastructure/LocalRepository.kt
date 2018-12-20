package com.happy.playground.infrastructure

interface LocalRepository {

    fun save(key: String, value: String?)
    fun save(key: String, value: Boolean)
    fun save(key: String, value: Int)
    fun save(key: String, value: Long)
    fun save(key: String, value: Float)
    fun get(key: String, defValue: String? = null): String?
    fun getBoolean(key: String, defValue: String? = null): Boolean
    fun getFloat(key: String, defValue: Float = -1f): Float
    fun getInt(key: String, defValue: Int = -1): Int
    fun getLong(key: String, defValue: Long = -1): Long
    fun clear(key: String)

    companion object {
        const val KEY = "key"
    }

}