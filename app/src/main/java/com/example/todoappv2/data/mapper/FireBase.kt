package com.example.todoappv2.data.mapper

import com.example.todoappv2.domain.UserModel
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserModel(): UserModel{
    return UserModel(
        id = uid,
        email = email,
        name = displayName
    )
}