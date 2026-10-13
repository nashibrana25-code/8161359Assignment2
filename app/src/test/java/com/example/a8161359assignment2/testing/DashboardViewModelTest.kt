package com.example.a8161359assignment2.testing

import com.example.a8161359assignment2.data.model.DashboardResponse
import com.example.a8161359assignment2.ui.dashboard.DashboardViewModel
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        repository = FakeAppRepository()
        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun successfulLoad_exposesEntitiesAndTotal() = runTest {
        viewModel.loadDashboard("history")

        assertTrue(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        assertEquals(
            repository.dashboardResponse.entities,
            viewModel.uiState.value.entities
        )
        assertEquals(1, viewModel.uiState.value.entityTotal)
        assertEquals("history", repository.lastKeypass)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun missingKeypass_showsError_withoutCallingRepository() = runTest {
        viewModel.loadDashboard("")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertEquals(0, repository.dashboardCallCount)
    }

    @Test
    fun networkFailure_showsError_andStopsLoading() = runTest {
        repository.dashboardException =
            IOException("Test connection failure")

        viewModel.loadDashboard("history")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.entities.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun alreadyLoadedKeypass_doesNotFetchAgain() = runTest {
        viewModel.loadDashboard("history")
        advanceUntilIdle()

        viewModel.loadDashboard("history")
        advanceUntilIdle()

        assertEquals(1, repository.dashboardCallCount)
    }

    @Test
    fun forceRefresh_fetchesAgain() = runTest {
        viewModel.loadDashboard("history")
        advanceUntilIdle()

        viewModel.loadDashboard(
            keypass = "history",
            forceRefresh = true
        )
        advanceUntilIdle()

        assertEquals(2, repository.dashboardCallCount)
    }

    @Test
    fun repeatedLoadDuringLoading_callsRepositoryOnce() = runTest {
        viewModel.loadDashboard("history")
        viewModel.loadDashboard("history")

        advanceUntilIdle()

        assertEquals(1, repository.dashboardCallCount)
    }

    @Test
    fun retryAfterFailure_loadsSuccessfully() = runTest {
        repository.dashboardException =
            IOException("Test connection failure")

        viewModel.loadDashboard("history")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)

        repository.dashboardException = null

        viewModel.loadDashboard(
            keypass = "history",
            forceRefresh = true
        )
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(1, viewModel.uiState.value.entities.size)
        assertEquals(2, repository.dashboardCallCount)
    }

    @Test
    fun emptyResponse_isSuccessfulWithEmptyList() = runTest {
        repository.dashboardResponse = DashboardResponse(
            entities = emptyList(),
            entityTotal = 0
        )

        viewModel.loadDashboard("history")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.entities.isEmpty())
        assertEquals(0, viewModel.uiState.value.entityTotal)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }
}