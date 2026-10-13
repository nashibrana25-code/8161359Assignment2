package com.example.a8161359assignment2.testing

import com.example.a8161359assignment2.data.model.DashboardResponse
import com.example.a8161359assignment2.data.model.HistoryEntity
import com.example.a8161359assignment2.data.model.LoginResponse
import com.example.a8161359assignment2.data.repository.AppRepository

class FakeAppRepository : AppRepository {

    var loginResponse = LoginResponse(keypass = "history")

    var dashboardResponse = DashboardResponse(
        entities = listOf(
            HistoryEntity(
                event = "Test Event",
                startYear = 1900,
                endYear = 1901,
                location = "Test Location",
                keyFigure = "Test Figure",
                description = "A description used only for testing."
            )
        ),
        entityTotal = 1
    )

    var loginException: Exception? = null
    var dashboardException: Exception? = null

    var loginCallCount = 0
    var dashboardCallCount = 0

    var lastUsername: String? = null
    var lastPassword: String? = null
    var lastKeypass: String? = null

    override suspend fun login(
        username: String,
        password: String
    ): LoginResponse {
        loginCallCount++
        lastUsername = username
        lastPassword = password

        loginException?.let { throw it }

        return loginResponse
    }

    override suspend fun getDashboard(
        keypass: String
    ): DashboardResponse {
        dashboardCallCount++
        lastKeypass = keypass

        dashboardException?.let { throw it }

        return dashboardResponse
    }
}