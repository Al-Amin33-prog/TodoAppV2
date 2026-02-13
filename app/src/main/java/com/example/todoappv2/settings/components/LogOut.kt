package com.example.todoappv2.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R

@Composable
fun LogOutRow(onLogoutClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onLogoutClick() },
        headlineContent = { 
            Text("Logout", color = MaterialTheme.colorScheme.error) 
        },
        supportingContent = { Text("Sign out of your account") },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.logout_24px),
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.error
            )
        }
    )
}
