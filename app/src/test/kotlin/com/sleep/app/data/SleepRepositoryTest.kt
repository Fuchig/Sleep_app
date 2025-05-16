package com.sleep.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class SleepRepositoryTest {

    private lateinit var sleepRecordDao: SleepRecordDao
    private lateinit var repository: SleepRepository
    private val testSleepRecords = MutableStateFlow<List<SleepRecord>>(emptyList())

    @Before
    fun setup() {
        sleepRecordDao = mock(SleepRecordDao::class.java)
        whenever(sleepRecordDao.getAllSleepRecords()).thenReturn(testSleepRecords)
        repository = SleepRepository(sleepRecordDao)
    }

    @Test
    fun insertSleepRecord_callsDaoInsert() = runTest {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        val expectedRecord = SleepRecord.create(startTime, endTime)
        whenever(sleepRecordDao.insert(any())).thenReturn(1L)

        // When
        val id = repository.insertSleepRecord(startTime, endTime)

        // Then
        assertEquals(1L, id)
        verify(sleepRecordDao).insert(any())
    }

    @Test
    fun getSleepRecordById_returnsSleepRecord_whenRecordExists() = runTest {
        // Given
        val id = 1L
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        val expectedRecord = SleepRecord(id, startTime, endTime, 510)
        whenever(sleepRecordDao.getSleepRecordById(id)).thenReturn(expectedRecord)

        // When
        val record = repository.getSleepRecordById(id)

        // Then
        assertEquals(expectedRecord, record)
    }

    @Test
    fun getSleepRecordById_returnsNull_whenRecordDoesNotExist() = runTest {
        // Given
        val id = 1L
        whenever(sleepRecordDao.getSleepRecordById(id)).thenReturn(null)

        // When
        val record = repository.getSleepRecordById(id)

        // Then
        assertNull(record)
    }

    @Test
    fun deleteSleepRecord_callsDaoDelete() = runTest {
        // Given
        val id = 1L
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        val record = SleepRecord(id, startTime, endTime, 510)

        // When
        repository.deleteSleepRecord(record)

        // Then
        verify(sleepRecordDao).delete(record)
    }

    @Test
    fun deleteSleepRecordById_callsDaoDeleteById() = runTest {
        // Given
        val id = 1L

        // When
        repository.deleteSleepRecordById(id)

        // Then
        verify(sleepRecordDao).deleteById(id)
    }

    @Test
    fun allSleepRecords_returnsFlowFromDao() = runTest {
        // Given
        val records = listOf(
            SleepRecord(1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(8), 480),
            SleepRecord(2, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(7), 420)
        )
        testSleepRecords.value = records

        // When
        val result = repository.allSleepRecords.first()

        // Then
        assertEquals(records, result)
    }
}