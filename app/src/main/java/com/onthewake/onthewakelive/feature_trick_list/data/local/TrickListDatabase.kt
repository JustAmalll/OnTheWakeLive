package com.onthewake.onthewakelive.feature_trick_list.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TrickListEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TrickListDatabase: RoomDatabase() {
    abstract val dao: TrickListDao
}