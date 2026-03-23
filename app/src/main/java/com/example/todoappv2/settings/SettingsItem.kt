package com.example.todoappv2.settings



data class SettingItem(
    val titleRes: Int,
    val subtitleRes: Int,
    val icon: Int,
    val route: String? = null,
    val isDestructive: Boolean = false
)
