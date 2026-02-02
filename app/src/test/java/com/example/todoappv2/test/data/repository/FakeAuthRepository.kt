package com.example.todoappv2.test.data.repository

import com.example.todoappv2.data.repository.AuthRepository
import com.example.todoappv2.domain.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository : AuthRepository {

    private val authFlow = MutableStateFlow<UserModel?>(null)
    private var fakeUser: UserModel? = null

    override val currentUser: UserModel?
        get() = fakeUser

    override fun observeAuthState(): Flow<UserModel?> = authFlow

    override suspend fun login(email: String, password: String): Result<UserModel> {
        return if (email == "test@mail.com" && password == "123456") {
            fakeUser = UserModel(
                id = "123",
                email = email,
                name = "Test User"
            )
            authFlow.value = fakeUser
            Result.success(fakeUser!!)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<UserModel> {
        fakeUser = UserModel("123", email,name)
        authFlow.value = fakeUser
        return Result.success(fakeUser!!)
    }

    override suspend fun logout() {
        fakeUser = null
        authFlow.value = null
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }
}