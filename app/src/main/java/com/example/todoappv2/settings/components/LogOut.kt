package com.example.todoappv2.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R

@Composable
fun LogOutRow(onLogoutClick: () -> Unit) {
    var showDialogue by remember { mutableStateOf(false) }
    if (showDialogue){
        AlertDialog(
            onDismissRequest = {showDialogue = false},
            title = { Text("Confirm Logout")},
            text = {Text("Are you sure you want to logout?")},
            confirmButton = {
                TextButton(onClick = {
                    showDialogue = false
                    onLogoutClick()
                }) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {showDialogue = false}
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    ListItem(
        modifier = Modifier.clickable { showDialogue = true },
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
