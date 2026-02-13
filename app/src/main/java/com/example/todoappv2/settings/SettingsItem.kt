package com.example.todoappv2.settings



data class SettingItem(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val route: String? = null,
    val isDestructive: Boolean = false
)
