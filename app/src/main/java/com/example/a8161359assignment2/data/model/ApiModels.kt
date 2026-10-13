package com.example.a8161359assignment2.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)

data class HistoryEntity(
    val event: String,
    val startYear: Int,
    val endYear: Int,
    val location: String,
    val keyFigure: String,
    val description: String
)

data class DashboardResponse(
    val entities: List<HistoryEntity>,
    val entityTotal: Int
)