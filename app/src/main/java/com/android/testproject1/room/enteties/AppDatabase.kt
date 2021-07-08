package com.android.testproject1.room.enteties

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [PostRoomEntity::class], version = 1)
//@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): PostsDao?

    companion object {
        const val DATABASE_NAME = "notesdatabase.db"

        @Volatile
        var instance: AppDatabase? = null
        private val LOCK = Any()
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(LOCK) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}