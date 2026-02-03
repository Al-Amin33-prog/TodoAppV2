package com.example.todoappv2.auth

import com.example.todoappv2.data.repository.AuthRepositoryImplementation
import com.example.todoappv2.util.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
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
class AuthIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var repository: AuthRepositoryImplementation
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        firebaseAuth = FirebaseAuth.getInstance()
        repository = AuthRepositoryImplementation(firebaseAuth)
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun register_updates_auth_state_real_repo() = runTest {
        viewModel.onEvent(
            AuthEvent.Register(
                name = "Test User",
                email = "integration@test.com",
                password = "123456"
            )
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.user)
        assertEquals(true, state.isLoggedIn)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun login_updates_auth_state_real_repo() = runTest {
        viewModel.onEvent(
            AuthEvent.Login(
                email = "integration@test.com",
                password = "123456"
            )
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.user)
        assertEquals(true, state.isLoggedIn)
    }

    @Test
    fun logout_clears_auth_state_real_repo() = runTest {
        viewModel.onEvent(AuthEvent.Logout)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNull(state.user)
        assertEquals(false, state.isLoggedIn)
    }
}