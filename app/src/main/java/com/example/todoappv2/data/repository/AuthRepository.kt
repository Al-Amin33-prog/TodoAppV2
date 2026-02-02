package com.example.todoappv2.data.repository

import com.example.todoappv2.domain.UserModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: UserModel?
    fun observeAuthState(): Flow<UserModel?>
    suspend fun login(
        email:String,
        password:String,): Result<UserModel>
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<UserModel>
    suspend fun logout()
    suspend fun resetPassword(email: String): Result<Unit>
}