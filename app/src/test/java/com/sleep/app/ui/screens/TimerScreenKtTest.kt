package com.sleep.app.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration

class TimerScreenKtTest {

    @Test
    fun formatDuration_zeroSeconds_returnsCorrectFormat() {
        // Given
        val duration = Duration.ZERO

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("00:00:00", formattedDuration)
    }

    @Test
    fun formatDuration_lessThanOneMinute_returnsCorrectFormat() {
        // Given
        val duration = Duration.ofSeconds(45)

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("00:00:45", formattedDuration)
    }

    @Test
    fun formatDuration_oneMinute_returnsCorrectFormat() {
        // Given
        val duration = Duration.ofMinutes(1)

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("00:01:00", formattedDuration)
    }

    @Test
    fun formatDuration_oneHour_returnsCorrectFormat() {
        // Given
        val duration = Duration.ofHours(1)

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("01:00:00", formattedDuration)
    }

    @Test
    fun formatDuration_complexDuration_returnsCorrectFormat() {
        // Given
        val duration = Duration.ofHours(2).plusMinutes(30).plusSeconds(15)

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("02:30:15", formattedDuration)
    }

    @Test
    fun formatDuration_moreThan24Hours_returnsCorrectFormat() {
        // Given
        val duration = Duration.ofHours(25).plusMinutes(15).plusSeconds(30)

        // When
        val formattedDuration = formatDuration(duration)

        // Then
        assertEquals("25:15:30", formattedDuration)
    }
}
