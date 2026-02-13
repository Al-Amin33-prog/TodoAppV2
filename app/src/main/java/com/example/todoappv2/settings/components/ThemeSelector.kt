package com.example.todoappv2.settings.components

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R

@Composable
fun ThemeSelectorRow(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text("Dark Mode") },
        supportingContent = { Text("Switch between light and dark theme") },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.palette),
                contentDescription = "Theme",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Switch(
                checked = isDarkMode,
                onCheckedChange = onThemeChange
            )
        }
    )
}
