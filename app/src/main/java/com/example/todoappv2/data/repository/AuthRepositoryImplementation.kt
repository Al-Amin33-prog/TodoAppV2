package com.example.todoappv2.data.repository

import com.example.todoappv2.data.mapper.toUserModel
import com.example.todoappv2.domain.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImplementation(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: UserModel?
        get() = auth.currentUser?.toUserModel()

    override fun observeAuthState(): Flow<UserModel?> = callbackFlow {
        trySend(auth.currentUser?.toUserModel())

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toUserModel())
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<UserModel> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("Login failed"))
            Result.success(user.toUserModel())
        } catch (e: Exception) {
            Result.failure(Exception("Login error: ${e.message}", e))
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<UserModel> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("Registration failed"))

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user.updateProfile(updates).await()

            Result.success(user.toUserModel())
        } catch (e: Exception) {
            Result.failure(Exception("Registration error: ${e.message}", e))
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Reset failed: ${e.message}", e))
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified?: false
    }
}