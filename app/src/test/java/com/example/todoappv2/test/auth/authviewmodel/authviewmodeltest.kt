package com.example.todoappv2.test.auth.authviewmodel

import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.test.data.repository.FakeAuthRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        repository = FakeAuthRepository()
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun login_success_updates_state() = runTest {
        viewModel.onEvent(AuthEvent.Login("test@mail.com", "123456"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.user)
        assertEquals(true, state.isLoggedIn)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun login_failure_sets_error() = runTest {
        viewModel.onEvent(AuthEvent.Login("wrong@mail.com", "0000"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Invalid credentials", state.error)
        assertEquals(false, state.isLoggedIn)
    }

    @Test
    fun register_success_updates_state() = runTest {
        viewModel.onEvent(AuthEvent.Register(name = "Amin", email = "new@mail.com", password = "123456"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.user)
        assertEquals(true, state.isLoggedIn)
    }

    @Test
    fun logout_clears_user() = runTest {
        viewModel.onEvent(AuthEvent.Login("test@mail.com", "123456"))
        advanceUntilIdle()

        viewModel.onEvent(AuthEvent.Logout)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.user)
        assertEquals(false, state.isLoggedIn)
    }

    @Test
    fun reset_password_clears_loading() = runTest {
        viewModel.onEvent(AuthEvent.ResetPassword("test@mail.com"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }
}
