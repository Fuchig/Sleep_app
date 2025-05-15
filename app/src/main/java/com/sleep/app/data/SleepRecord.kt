package com.sleep.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "sleep_records")
data class SleepRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val durationMinutes: Long // Duration in minutes
) {
    companion object {
        fun create(startTime: LocalDateTime, endTime: LocalDateTime): SleepRecord {
            val duration = Duration.between(startTime, endTime)
            val durationMinutes = duration.toMinutes()
            return SleepRecord(
                startTime = startTime,
                endTime = endTime,
                durationMinutes = durationMinutes
            )
        }
    }
}