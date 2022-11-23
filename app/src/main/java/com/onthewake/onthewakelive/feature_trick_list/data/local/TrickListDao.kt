package com.onthewake.onthewakelive.feature_trick_list.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrickListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrickList(trickList: TrickListEntity)

    @Query("DELETE FROM tricklistentity")
    suspend fun deleteTrickList()

    @Query("SELECT * FROM tricklistentity")
    suspend fun getTrickList(): TrickListEntity?
}