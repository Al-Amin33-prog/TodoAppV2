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
import com.example.todoappv2.R
import com.example.todoappv2.core.navigation.Routes

@Composable
fun AppBottomBar(
    navController: NavController
) {
    fun getIconForRoute(route: String?): Int {
        return when {
            route?.startsWith(Routes.SUBJECTS) == true -> R.drawable.ic_book
            route?.startsWith("tasks") == true -> R.drawable.ic_task
            route == Routes.HOME -> R.drawable.ic_home_work
            route == Routes.STATS -> R.drawable.ic_bar_chart
            route == Routes.NOTIFICATIONS -> R.drawable.ic_notification
            else -> R.drawable.ic_home_work
        }
    }

    val items = listOf(
        Routes.HOME,
        Routes.SUBJECTS,
        Routes.NOTIFICATIONS,
        Routes.STATS
    )

    val currentRoute = navController.currentBackStackEntryAsState().value
        ?.destination?.route

    NavigationBar {
        items.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(Routes.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = getIconForRoute(route)),
                        contentDescription = null,
                        tint = if (currentRoute == route){
                            MaterialTheme.colorScheme.primary
                        }else{
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                },
                label = { 
                    val label = when(route) {
                        Routes.HOME -> "Home"
                        Routes.SUBJECTS -> "Subjects"
                        Routes.NOTIFICATIONS -> "Alerts"
                        Routes.STATS -> "Stats"
                        else -> route
                    }
                    Text(label, style = MaterialTheme.typography.labelSmall)
                }
            )
        }
    }
}
