package com.sleep.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepRecordDao {
    @Insert
    suspend fun insert(sleepRecord: SleepRecord): Long

    @Update
    suspend fun update(sleepRecord: SleepRecord)

    @Query("SELECT * FROM sleep_records ORDER BY startTime DESC")
    fun getAllSleepRecords(): Flow<List<SleepRecord>>

    @Query("SELECT * FROM sleep_records WHERE id = :id")
    suspend fun getSleepRecordById(id: Long): SleepRecord?

    @Delete
    suspend fun delete(sleepRecord: SleepRecord)

    @Query("DELETE FROM sleep_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}
