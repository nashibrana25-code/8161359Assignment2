package com.example.a8161359assignment2.ui.dashboard

import com.example.a8161359assignment2.data.model.HistoryEntity

data class DashboardUiState(
    val isLoading: Boolean = false,
    val entities: List<HistoryEntity> = emptyList(),
    val entityTotal: Int = 0,
    val errorMessage: String? = null
)