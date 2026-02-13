package com.example.todoappv2.settings.components

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R

@Composable
fun HelpAndFeedbackRow() {
    ListItem(
        headlineContent = { Text("Help & Feedback") },
        supportingContent = { Text("Contact support or report a bug") },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.help_24px),
                contentDescription = "Help",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}
