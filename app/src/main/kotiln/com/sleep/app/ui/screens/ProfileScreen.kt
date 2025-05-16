package com.sleep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sleep.app.ui.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = viewModel()
) {
    val babyName by profileViewModel.babyName.collectAsState()
    val dateOfBirth by profileViewModel.dateOfBirth.collectAsState()

    // Format for displaying the date
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    // State for date input
    var day by remember { mutableStateOf(dateOfBirth.dayOfMonth.toString()) }
    var month by remember { mutableStateOf(dateOfBirth.monthValue.toString()) }
    var year by remember { mutableStateOf(dateOfBirth.year.toString()) }

    // Function to update date of birth
    fun updateDateOfBirth() {
        try {
            val d = day.toIntOrNull() ?: 1
            val m = month.toIntOrNull() ?: 1
            val y = year.toIntOrNull() ?: 2000

            // Validate date values
            val validDay = d.coerceIn(1, 31)
            val validMonth = m.coerceIn(1, 12)
            val validYear = y.coerceIn(1900, 2100)

            try {
                val newDate = LocalDate.of(validYear, validMonth, validDay)
                profileViewModel.updateDateOfBirth(newDate)
            } catch (e: Exception) {
                // Handle invalid date (e.g., February 30)
                profileViewModel.updateDateOfBirth(LocalDate.of(validYear, 1, 1))
            }
        } catch (e: Exception) {
            // Handle any other errors
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Avatar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name field
            OutlinedTextField(
                value = babyName,
                onValueChange = { newValue -> 
                    profileViewModel.updateName(newValue)
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Date of Birth fields
            Text(
                text = "Date of Birth",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Month field (MM)
                OutlinedTextField(
                    value = month,
                    onValueChange = { 
                        if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                            month = it
                            updateDateOfBirth()
                        }
                    },
                    label = { Text("MM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                // Day field (DD)
                OutlinedTextField(
                    value = day,
                    onValueChange = { 
                        if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                            day = it
                            updateDateOfBirth()
                        }
                    },
                    label = { Text("DD") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                // Year field (YYYY)
                OutlinedTextField(
                    value = year,
                    onValueChange = { 
                        if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                            year = it
                            updateDateOfBirth()
                        }
                    },
                    label = { Text("YYYY") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1.5f)
                )
            }

            // Display formatted date
            Text(
                text = "Selected date: ${dateOfBirth.format(dateFormatter)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            Button(
                onClick = { profileViewModel.saveProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Profile")
            }
        }
    }
}
