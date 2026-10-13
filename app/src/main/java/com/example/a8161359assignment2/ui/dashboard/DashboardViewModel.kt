package com.example.a8161359assignment2.ui.dashboard

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
class DashboardViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var loadedKeypass: String? = null

    fun loadDashboard(
        keypass: String,
        forceRefresh: Boolean = false
    ) {
        if (_uiState.value.isLoading) return

        if (keypass.isBlank()) {
            _uiState.value = DashboardUiState(
                errorMessage = "Missing login keypass. Please log in again."
            )
            return
        }

        if (!forceRefresh && loadedKeypass == keypass) return

        _uiState.value = DashboardUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getDashboard(keypass)

                loadedKeypass = keypass

                _uiState.value = DashboardUiState(
                    entities = response.entities,
                    entityTotal = response.entityTotal
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: HttpException) {
                val message = when (exception.code()) {
                    401, 403 ->
                        "Access was rejected. Please log in again."

                    404 ->
                        "No dashboard was found for this keypass."

                    in 500..599 ->
                        "The server is unavailable. Please try again later."

                    else ->
                        "Could not load events (HTTP ${exception.code()})."
                }

                _uiState.value = DashboardUiState(
                    errorMessage = message
                )
            } catch (exception: IOException) {
                _uiState.value = DashboardUiState(
                    errorMessage = "Could not reach the server. Check your connection and retry."
                )
            } catch (exception: Exception) {
                _uiState.value = DashboardUiState(
                    errorMessage = "An unexpected error occurred while loading events."
                )
            }
        }
    }
}