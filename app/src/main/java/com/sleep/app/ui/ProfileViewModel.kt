package com.sleep.app.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    // User login information
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    // Baby information
    private val _babyName = MutableStateFlow("")
    val babyName: StateFlow<String> = _babyName.asStateFlow()

    private val _dateOfBirth = MutableStateFlow(LocalDate.now().minusYears(1))
    val dateOfBirth: StateFlow<LocalDate> = _dateOfBirth.asStateFlow()

    // Initialize with saved data if available
    init {
        // Load data from SharedPreferences
        _username.value = sharedPreferences.getString("username", "") ?: ""
        _email.value = sharedPreferences.getString("email", "") ?: ""
        _babyName.value = sharedPreferences.getString("baby_name", "") ?: ""

        val savedDateStr = sharedPreferences.getString("date_of_birth", null)
        _dateOfBirth.value = if (savedDateStr != null) {
            LocalDate.parse(savedDateStr, dateFormatter)
        } else {
            LocalDate.now().minusYears(1)
        }
    }

    fun updateName(newName: String) {
        _babyName.value = newName
    }

    fun updateDateOfBirth(date: LocalDate) {
        _dateOfBirth.value = date
    }

    fun saveProfile() {
        viewModelScope.launch {
            // Save to SharedPreferences
            sharedPreferences.edit().apply {
                putString("baby_name", _babyName.value)
                putString("date_of_birth", _dateOfBirth.value.format(dateFormatter))
                apply()
            }
            println("Saving profile: name=${_babyName.value}, dateOfBirth=${_dateOfBirth.value}")
        }
    }
}
