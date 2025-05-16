package com.sleep.app.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SleepRepository(private val sleepRecordDao: SleepRecordDao) {

    val allSleepRecords: Flow<List<SleepRecord>> = sleepRecordDao.getAllSleepRecords()

    suspend fun insertSleepRecord(startTime: LocalDateTime, endTime: LocalDateTime): Long {
        val sleepRecord = SleepRecord.create(startTime, endTime)
        return sleepRecordDao.insert(sleepRecord)
    }

    suspend fun getSleepRecordById(id: Long): SleepRecord? {
        return sleepRecordDao.getSleepRecordById(id)
    }

    suspend fun updateSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDao.update(sleepRecord)
    }

    suspend fun deleteSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDao.delete(sleepRecord)
    }

    suspend fun deleteSleepRecordById(id: Long) {
        sleepRecordDao.deleteById(id)
    }
}
