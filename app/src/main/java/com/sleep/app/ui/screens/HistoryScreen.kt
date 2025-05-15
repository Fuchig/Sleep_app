package com.sleep.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sleep.app.data.SleepRecord
import com.sleep.app.ui.SleepViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: SleepViewModel = viewModel()
) {
    val sleepRecords by viewModel.allSleepRecords.collectAsState(initial = emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var currentEditRecord by remember { mutableStateOf<SleepRecord?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sleep History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (sleepRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sleep records yet.\nStart tracking your sleep!",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sleepRecords) { record ->
                    SleepRecordItem(
                        sleepRecord = record,
                        onDelete = { viewModel.deleteSleepRecord(record.id) },
                        onEdit = { 
                            currentEditRecord = it
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }

    // Show edit dialog if a record is selected for editing
    if (showEditDialog && currentEditRecord != null) {
        EditSleepRecordDialog(
            sleepRecord = currentEditRecord!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedRecord ->
                viewModel.updateSleepRecord(updatedRecord)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun SleepRecordItem(
    sleepRecord: SleepRecord,
    onDelete: () -> Unit,
    onEdit: (SleepRecord) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sleepRecord.startTime.format(dateFormatter),
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    IconButton(onClick = { onEdit(sleepRecord) }) {
                        Text("Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Text("Delete")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Start Time")
                    Text(
                        text = sleepRecord.startTime.format(timeFormatter),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column {
                    Text("End Time")
                    Text(
                        text = sleepRecord.endTime.format(timeFormatter),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column {
                    Text("Duration")
                    Text(
                        text = "${sleepRecord.durationMinutes / 60}h ${sleepRecord.durationMinutes % 60}m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSleepRecordDialog(
    sleepRecord: SleepRecord,
    onDismiss: () -> Unit,
    onConfirm: (SleepRecord) -> Unit
) {
    var startHour by remember { mutableStateOf(sleepRecord.startTime.hour.toString()) }
    var startMinute by remember { mutableStateOf(sleepRecord.startTime.minute.toString()) }
    var endHour by remember { mutableStateOf(sleepRecord.endTime.hour.toString()) }
    var endMinute by remember { mutableStateOf(sleepRecord.endTime.minute.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Edit Sleep Record",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Start time section
                Text(
                    text = "Sleep Start Time",
                    style = MaterialTheme.typography.titleMedium
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
                    style = MaterialTheme.typography.titleMedium
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

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val startTimeHour = startHour.toIntOrNull() ?: sleepRecord.startTime.hour
                            val startTimeMinute = startMinute.toIntOrNull() ?: sleepRecord.startTime.minute
                            val endTimeHour = endHour.toIntOrNull() ?: sleepRecord.endTime.hour
                            val endTimeMinute = endMinute.toIntOrNull() ?: sleepRecord.endTime.minute

                            val newStartTime = sleepRecord.startTime
                                .withHour(startTimeHour)
                                .withMinute(startTimeMinute)
                            val newEndTime = sleepRecord.endTime
                                .withHour(endTimeHour)
                                .withMinute(endTimeMinute)

                            val duration = Duration.between(newStartTime, newEndTime)
                            val durationMinutes = duration.toMinutes()

                            if (durationMinutes > 0) {
                                val updatedRecord = sleepRecord.copy(
                                    startTime = newStartTime,
                                    endTime = newEndTime,
                                    durationMinutes = durationMinutes
                                )
                                onConfirm(updatedRecord)
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
