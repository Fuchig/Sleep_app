package com.sleep.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sleep.app.ui.SleepViewModel
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: SleepViewModel = viewModel()
) {
    val timerState by viewModel.timerState.collectAsState()
    var elapsedTime by remember { mutableStateOf(Duration.ZERO) }
    val scrollState = rememberScrollState()

    // Manual sleep logging state
    var showManualLogging by remember { mutableStateOf(false) }

    // Date state
    val today = LocalDate.now()
    var startDate by remember { mutableStateOf(today) }
    var endDate by remember { mutableStateOf(today) }

    // Time state
    var startHour by remember { mutableStateOf("22") }
    var startMinute by remember { mutableStateOf("00") }
    var endHour by remember { mutableStateOf("06") }
    var endMinute by remember { mutableStateOf("00") }

    // Update elapsed time every second when timer is running
    LaunchedEffect(timerState) {
        if (timerState is SleepViewModel.TimerState.Running) {
            while (true) {
                elapsedTime = (timerState as SleepViewModel.TimerState.Running).getElapsedTime()
                delay(1000) // Update every second
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sleep Timer") },
                actions = {
                    TextButton(onClick = onNavigateToHistory) {
                        Text("History")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display timer
            Text(
                text = formatDuration(elapsedTime),
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Start/Stop button
            Button(
                onClick = {
                    if (timerState is SleepViewModel.TimerState.Running) {
                        viewModel.stopSleepTimer()
                    } else {
                        viewModel.startSleepTimer()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (timerState is SleepViewModel.TimerState.Running) "Stop" else "Start",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Manual sleep logging section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // Toggle button to show/hide manual logging form
                    Button(
                        onClick = { showManualLogging = !showManualLogging },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showManualLogging) "Hide" else "Add manual entry")
                    }

                    if (showManualLogging) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Start time section
                        Text(
                            text = "Sleep Start Time",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Start time inputs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Hour input
                            OutlinedTextField(
                                value = startHour,
                                onValueChange = { 
                                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                        val value = it.toIntOrNull() ?: 0
                                        if (value in 0..23 || it.isEmpty()) {
                                            startHour = it
                                        }
                                    }
                                },
                                label = { Text("Hour (0-23)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            // Minute input
                            OutlinedTextField(
                                value = startMinute,
                                onValueChange = { 
                                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                        val value = it.toIntOrNull() ?: 0
                                        if (value in 0..59 || it.isEmpty()) {
                                            startMinute = it
                                        }
                                    }
                                },
                                label = { Text("Minute (0-59)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // End time section
                        Text(
                            text = "Sleep End Time",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // End time inputs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Hour input
                            OutlinedTextField(
                                value = endHour,
                                onValueChange = { 
                                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                        val value = it.toIntOrNull() ?: 0
                                        if (value in 0..23 || it.isEmpty()) {
                                            endHour = it
                                        }
                                    }
                                },
                                label = { Text("Hour (0-23)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            // Minute input
                            OutlinedTextField(
                                value = endMinute,
                                onValueChange = { 
                                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                        val value = it.toIntOrNull() ?: 0
                                        if (value in 0..59 || it.isEmpty()) {
                                            endMinute = it
                                        }
                                    }
                                },
                                label = { Text("Minute (0-59)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Save button
                        Button(
                            onClick = {
                                // Create LocalDateTime objects for start and end times
                                val startTimeHour = startHour.toIntOrNull() ?: 0
                                val startTimeMinute = startMinute.toIntOrNull() ?: 0
                                val endTimeHour = endHour.toIntOrNull() ?: 0
                                val endTimeMinute = endMinute.toIntOrNull() ?: 0

                                val startDateTime = LocalDateTime.of(
                                    startDate,
                                    LocalTime.of(startTimeHour, startTimeMinute)
                                )

                                val endDateTime = LocalDateTime.of(
                                    endDate,
                                    LocalTime.of(endTimeHour, endTimeMinute)
                                )

                                // If end time is before start time, assume it's the next day
                                var adjustedEndDateTime = endDateTime
                                if (endDateTime.isBefore(startDateTime)) {
                                    adjustedEndDateTime = LocalDateTime.of(
                                        endDate.plusDays(1),
                                        LocalTime.of(endTimeHour, endTimeMinute)
                                    )
                                }

                                // Log the sleep record
                                viewModel.logManualSleep(startDateTime, adjustedEndDateTime)

                                // Hide the form after saving
                                showManualLogging = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("Save Sleep Record")
                        }
                    }
                }
            }
        }
    }
}

internal fun formatDuration(duration: Duration): String {
    val hours = duration.toHours()
    val minutes = duration.toMinutesPart()
    val seconds = duration.toSecondsPart()

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
