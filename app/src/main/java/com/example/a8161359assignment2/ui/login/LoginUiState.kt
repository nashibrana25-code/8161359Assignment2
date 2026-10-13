package com.example.a8161359assignment2.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val keypass: String? = null,
    val errorMessage: String? = null
)