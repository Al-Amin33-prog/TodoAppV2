package com.example.todoappv2.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.todoappv2.core.navigation.AppNavGraph
import com.example.todoappv2.core.notification.TaskNotificationChannel
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.datastore.SettingsManager
import com.example.todoappv2.ui.theme.TodoAppV2Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var settingsManager: SettingsManager
    @Inject lateinit var schedule: TaskReminderSchedule
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        TaskNotificationChannel.create(this)
        enableEdgeToEdge()


        
        setContent {
            val isDarkMode by settingsManager.isDarkMode.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            TodoAppV2Theme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                

                AppNavGraph(
                    rootNavController = navController,
                    schedule = schedule,
                    isDarkMode = isDarkMode,
                    onThemeChange = { enabled ->
                        scope.launch {
                            settingsManager.setDarkMode(enabled)
                        }
                    }
                )
            }
        }
    }
}
