package com.example.todoappv2.core.components


import com.example.todoappv2.core.navigation.Routes
import com.example.todoappv2.R

sealed class BottomNavItem(
    val route: String,
    val label:String,
    val icon: Int
) {
    object Home: BottomNavItem(
        route = Routes.HOME,
        label = "Home",
        icon =  R.drawable.ic_home_work

    )
    object Subjects: BottomNavItem(
        route = Routes.SUBJECTS,
        label = "Subjects",
        icon = R.drawable.ic_book
    )
    object Tasks: BottomNavItem(
        route =  Routes.TASKS_ROOT,
        label = "Tasks",
        icon = R.drawable.ic_task
    )
    object Stats: BottomNavItem(
        route = Routes.STATS,
        label = "Stats",
        icon = R.drawable.ic_bar_chart
    )

}