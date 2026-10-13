package com.example.a8161359assignment2.data.repository

import com.example.a8161359assignment2.data.model.DashboardResponse
import com.example.a8161359assignment2.data.model.LoginResponse

interface AppRepository {

    suspend fun login(
        username: String,
        password: String
    ): LoginResponse

    suspend fun getDashboard(
        keypass: String
    ): DashboardResponse
}