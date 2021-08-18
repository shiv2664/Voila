package com.android.testproject1.typeconverters

import androidx.room.TypeConverter
import java.util.*

class TypeConvertersClass {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }


}