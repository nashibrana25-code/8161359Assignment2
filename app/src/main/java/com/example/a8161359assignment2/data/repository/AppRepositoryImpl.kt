package com.example.a8161359assignment2.data.repository

import com.example.a8161359assignment2.data.model.DashboardResponse
import com.example.a8161359assignment2.data.model.LoginRequest
import com.example.a8161359assignment2.data.model.LoginResponse
import com.example.a8161359assignment2.data.remote.ApiService
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AppRepository {

    override suspend fun login(
        username: String,
        password: String
    ): LoginResponse {
        return apiService.login(
            LoginRequest(
                username = username,
                password = password
            )
        )
    }

    override suspend fun getDashboard(
        keypass: String
    ): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}