package com.example.todoappv2.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.todoappv2.core.navigation.Routes

@Composable
fun AppBottomBar(
    navController: NavController
){
    val items = listOf(
        Routes.HOME,
        Routes.SUBJECTS,
        Routes.NOTIFICATIONS,
        Routes.STATS,
        Routes.SETTINGS
    )
    NavigationBar {
        items.forEach { route ->
            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate(route){
                        popUpTo(Routes.HOME)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(getIconForRoute(route),null) },
                label = { Text(route.uppercase()) }
            )

        }
    }

}