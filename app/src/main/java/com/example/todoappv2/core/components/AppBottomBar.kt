package com.example.todoappv2.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState

import com.example.todoappv2.core.navigation.Routes

@Composable
fun AppBottomBar(
    navController: NavController
) {



    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Subjects,
        BottomNavItem.Tasks,
        BottomNavItem.Stats
    )

    val currentRoute = navController.currentBackStackEntryAsState()
        .value
        ?.destination?.route
    NavigationBar {
        items.forEach { item  ->
            val isSelected = when(item.route){
                Routes.TASKS_ROOT ->
                currentRoute?.startsWith(Routes.TASKS_ROOT) == true
                else ->
                    currentRoute == item.route
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {

                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }

    }

