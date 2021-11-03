package com.android.testproject1.room.enteties

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.testproject1.typeconverters.TypeConvertersClass


@Database(entities = [PostRoomEntity::class,UsersChatListEntity::class,OffersSavedRoomEntity::class,ChatRoomEntity::class], version = 4)
@TypeConverters(TypeConvertersClass::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao?

    companion object {
        private const val DATABASE_NAME = "notesdatabase.db"

//        private val migration1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "CREATE TABLE `OffersSavedRoomEntity` (`postId` TEXT NOT NULL," +
//                            "`userId` TEXT NOT NULL,`name` TEXT NOT NULL,`timestamp` TEXT NOT NULL," +
//                            "`likes` TEXT  NOT NULL,`favourites` TEXT  NOT NULL," +
//                            "`description` TEXT NOT NULL,`username` TEXT  NOT NULL," +
//                            "`userimage` TEXT NOT NULL,`title` TEXT NOT NULL," +
//                            "`originalPrice` TEXT NOT NULL,`discountPrice` TEXT NOT NULL," +
//                            "`city` TEXT NOT NULL,`ratings` TEXT NOT NULL," +
//                            "`usersRated` TEXT  NOT NULL,`addressMap` TEXT   NOT NULL," +
//                            "`offerCategory` TEXT NOT NULL,`minPeople` TEXT NOT NULL," +
//                            "`maxPeople` TEXT   NOT NULL,`image_count` INTEGER Default(0) NOT NULL," +
//                            "`image_url_0` TEXT NOT NULL,`image_url_1` TEXT NOT NULL," +
//                            "`image_url_2` TEXT NOT NULL,`image_url_3` TEXT NOT NULL," +
//                            "`image_url_4` TEXT NOT NULL,`image_url_5` TEXT NOT NULL," +
//                            "`image_url_6` TEXT NOT NULL,PRIMARY KEY(`postId`))"
//                )
//            }
//        }

//            private val migration2_3: Migration = object : Migration(2, 3) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("Create Table `ChatRoomEntity` (`sender` TEXT NOT NULL,`receiver` TEXT NOT NULL," +
//                            "`message` TEXT NOT NULL,`isseen` INTEGER DEFAULT(0),`MessageId` TEXT NOT NULL," +
//                            "`name` TEXT NOT NULL ,`timestamp` Date NOT NULL),PRIMARY KEY(`id`)")
//                }
//
//            }
//
//        private val migration3_4: Migration = object : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("Alter Table `ChatRoomEntity`ADD `id` TEXT ")
//            }

//        }

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
                            )
//                                .addMigrations(migration1_2,migration2_3, migration3_4)
                                .build()
                        }
                    }
                }
                return instance
            }
        }
    }
