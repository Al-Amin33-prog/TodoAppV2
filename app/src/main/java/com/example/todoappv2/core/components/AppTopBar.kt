package com.example.todoappv2.core.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.todoappv2.R
import com.example.todoappv2.core.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController){
    TopAppBar(
        title = {
            Text("HOME",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Routes.SETTINGS)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
