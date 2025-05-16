package com.sleep.app.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class SleepRecordTest {

    @Test
    fun create_calculatesCorrectDuration_whenEndTimeIsAfterStartTime() {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        
        // When
        val sleepRecord = SleepRecord.create(startTime, endTime)
        
        // Then
        assertEquals(510, sleepRecord.durationMinutes) // 8 hours and 30 minutes = 510 minutes
    }
    
    @Test
    fun create_calculatesCorrectDuration_forShortDuration() {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 1, 22, 30)
        
        // When
        val sleepRecord = SleepRecord.create(startTime, endTime)
        
        // Then
        assertEquals(30, sleepRecord.durationMinutes) // 30 minutes
    }
    
    @Test
    fun create_calculatesCorrectDuration_forLongDuration() {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 20, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 20, 0)
        
        // When
        val sleepRecord = SleepRecord.create(startTime, endTime)
        
        // Then
        assertEquals(1440, sleepRecord.durationMinutes) // 24 hours = 1440 minutes
    }
    
    @Test
    fun create_setsCorrectStartAndEndTimes() {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        
        // When
        val sleepRecord = SleepRecord.create(startTime, endTime)
        
        // Then
        assertEquals(startTime, sleepRecord.startTime)
        assertEquals(endTime, sleepRecord.endTime)
    }
    
    @Test
    fun create_setsIdToZero() {
        // Given
        val startTime = LocalDateTime.of(2023, 1, 1, 22, 0)
        val endTime = LocalDateTime.of(2023, 1, 2, 6, 30)
        
        // When
        val sleepRecord = SleepRecord.create(startTime, endTime)
        
        // Then
        assertEquals(0L, sleepRecord.id)
    }
}