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
fun AppTopBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,

){
    TopAppBar(
        title = {
            Text(text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
       navigationIcon = {
           if (showBackButton){
               IconButton(onClick = onBackClick) {
                   Icon(
                       painter = painterResource(R.drawable.arrow_back_24px__1_),
                       contentDescription = "Back"
                   )
               }
           }
       },
        actions = {
            if (!showBackButton){
                IconButton(onClick = onSettingClick) {
                    Icon(painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings"
                    )
                }
            }
        }
    )
}
