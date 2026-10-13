package com.example.a8161359assignment2.testing

import com.example.a8161359assignment2.ui.login.LoginViewModel
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
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        repository = FakeAppRepository()
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun emptyInputs_showError_withoutCallingRepository() = runTest {
        viewModel.login("", "")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(0, repository.loginCallCount)
    }

    @Test
    fun studentIdWithLetter_showError_withoutCallingRepository() = runTest {
        viewModel.login("s1234567", "TestName")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertEquals(0, repository.loginCallCount)
    }

    @Test
    fun successfulLogin_exposesKeypass_andPreservesPasswordCase() = runTest {
        viewModel.login(" 1234567 ", "TestName")

        assertTrue(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        assertEquals("history", viewModel.uiState.value.keypass)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)

        assertEquals("1234567", repository.lastUsername)
        assertEquals("TestName", repository.lastPassword)
        assertEquals(1, repository.loginCallCount)
    }

    @Test
    fun networkFailure_showsError_andStopsLoading() = runTest {
        repository.loginException = IOException("Test connection failure")

        viewModel.login("1234567", "TestName")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.keypass)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun emptyKeypass_showsError() = runTest {
        repository.loginResponse =
            com.example.a8161359assignment2.data.model.LoginResponse("")

        viewModel.login("1234567", "TestName")
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.keypass)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun repeatedLoginDuringLoading_callsRepositoryOnce() = runTest {
        viewModel.login("1234567", "TestName")
        viewModel.login("1234567", "TestName")

        advanceUntilIdle()

        assertEquals(1, repository.loginCallCount)
    }

    @Test
    fun navigationHandled_clearsKeypass() = runTest {
        viewModel.login("1234567", "TestName")
        advanceUntilIdle()

        assertEquals("history", viewModel.uiState.value.keypass)

        viewModel.onNavigationHandled()

        assertNull(viewModel.uiState.value.keypass)
    }
}