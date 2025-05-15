package com.sleep.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sleep.app.data.SleepDatabase
import com.sleep.app.data.SleepRecord
import com.sleep.app.data.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class SleepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SleepRepository
    val allSleepRecords: Flow<List<SleepRecord>>

    // Timer state
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    // Sleep start time
    private var sleepStartTime: LocalDateTime? = null

    init {
        val sleepRecordDao = SleepDatabase.getDatabase(application).sleepRecordDao()
        repository = SleepRepository(sleepRecordDao)
        allSleepRecords = repository.allSleepRecords
    }

    fun startSleepTimer() {
        sleepStartTime = LocalDateTime.now()
        _timerState.value = TimerState.Running(sleepStartTime!!)
    }

    fun stopSleepTimer() {
        val startTime = sleepStartTime
        if (startTime != null) {
            val endTime = LocalDateTime.now()
            viewModelScope.launch {
                repository.insertSleepRecord(startTime, endTime)
            }
            _timerState.value = TimerState.Stopped
            sleepStartTime = null
        }
    }

    fun logManualSleep(startTime: LocalDateTime, endTime: LocalDateTime) {
        viewModelScope.launch {
            repository.insertSleepRecord(startTime, endTime)
        }
    }

    fun deleteSleepRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteSleepRecordById(id)
        }
    }

    fun updateSleepRecord(sleepRecord: SleepRecord) {
        viewModelScope.launch {
            repository.updateSleepRecord(sleepRecord)
        }
    }

    sealed class TimerState {
        object Stopped : TimerState()
        data class Running(val startTime: LocalDateTime) : TimerState() {
            fun getElapsedTime(): Duration {
                return Duration.between(startTime, LocalDateTime.now())
            }
        }
    }
}
