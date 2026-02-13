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
fun SyncToggleRow(
    isSyncEnabled: Boolean,
    onToggleSync: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text("Cloud Sync") },
        supportingContent = { Text("Sync your tasks with Firebase") },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.sync_24px__1_),
                contentDescription = "Sync",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Switch(
                checked = isSyncEnabled,
                onCheckedChange = onToggleSync
            )
        }
    )
}
