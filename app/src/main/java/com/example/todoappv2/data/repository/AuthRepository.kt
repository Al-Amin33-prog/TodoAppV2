package com.example.todoappv2.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun observeAuthState(): Flow<FirebaseUser?>
    suspend fun login(
        email:String,
        password:String,): Result<FirebaseUser>
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<FirebaseUser>
    suspend fun logout()
    suspend fun resetPassword(email: String): Result<Unit>
}