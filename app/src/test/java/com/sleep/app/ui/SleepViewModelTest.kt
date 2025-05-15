package com.sleep.app.ui

import com.sleep.app.data.SleepRecord
import com.sleep.app.data.SleepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

/**
 * Unit tests for the SleepViewModel's TimerState class.
 * 
 * Since SleepViewModel is final and requires an Application context,
 * we're focusing on testing the TimerState class separately, which contains
 * the core logic for calculating elapsed time.
 */
@ExperimentalCoroutinesApi
class SleepViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun timerState_running_calculatesElapsedTimeCorrectly() = runTest {
        // Given
        val startTime = LocalDateTime.now().minusMinutes(10)
        val runningState = SleepViewModel.TimerState.Running(startTime)

        // When
        val elapsedTime = runningState.getElapsedTime()

        // Then
        // Allow for a small difference due to test execution time
        assertTrue(elapsedTime.toMinutes() >= 9 && elapsedTime.toMinutes() <= 11)
    }
}
