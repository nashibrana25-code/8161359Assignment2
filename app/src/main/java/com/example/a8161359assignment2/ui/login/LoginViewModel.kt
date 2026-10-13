package com.example.a8161359assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a8161359assignment2.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        if (_uiState.value.isLoading) return

        val studentId = username.trim()

        if (studentId.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                errorMessage = "Enter your student ID and first-name password."
            )
            return
        }

        if (!studentId.all { it.isDigit() }) {
            _uiState.value = LoginUiState(
                errorMessage = "Use your student ID without the letter s."
            )
            return
        }

        _uiState.value = LoginUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.login(studentId, password)

                if (response.keypass.isBlank()) {
                    _uiState.value = LoginUiState(
                        errorMessage = "The server returned an empty keypass."
                    )
                } else {
                    _uiState.value = LoginUiState(
                        keypass = response.keypass
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: HttpException) {
                val message = when (exception.code()) {
                    400 -> "The login request was rejected. Check your details."
                    401, 403 -> "Login rejected. Check your student ID and password case."
                    in 500..599 -> "The server is unavailable. Try again later."
                    else -> "Login failed (HTTP ${exception.code()})."
                }

                _uiState.value = LoginUiState(errorMessage = message)
            } catch (exception: IOException) {

                _uiState.value = LoginUiState(
                    errorMessage = "Could not reach the server. Check your connection and try again."
                )
            } catch (exception: Exception) {
                _uiState.value = LoginUiState(
                    errorMessage = "An unexpected error occurred. Please try again."
                )
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(keypass = null)
    }
}