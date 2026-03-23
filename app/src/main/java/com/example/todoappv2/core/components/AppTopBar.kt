package com.example.todoappv2.core.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.todoappv2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: @Composable () -> Unit,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,

){
    TopAppBar(
        title = title,
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
