package com.example.todoappv2.settings.components

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R

@Composable
fun AboutRow() {
    ListItem(
        headlineContent = { Text("About") },
        supportingContent = { Text("App version 1.0.0") },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "About",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}
