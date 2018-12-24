package com.happy.playground.repository.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class MockableDatabase : RoomDatabase() {




    companion object {

        private const val DATABASE_NAME = "mockable.db"

        @Volatile private var instance: MockableDatabase? = null

        fun getInstance(context: Context): MockableDatabase {
            return instance ?: synchronized(this) {
                instance?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MockableDatabase {
            return Room.databaseBuilder(
                context, MockableDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}