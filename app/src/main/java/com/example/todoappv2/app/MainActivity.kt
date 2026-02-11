package com.example.todoappv2.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.core.components.AppBottomBar
import com.example.todoappv2.core.navigation.AppNavGraph
import com.example.todoappv2.core.notification.TaskNotificationChannel
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.database.AppDataBase
import com.example.todoappv2.data.repository.AppRepositoryImplementation
import com.example.todoappv2.data.repository.AuthRepositoryImplementation
import com.example.todoappv2.ui.theme.TodoAppV2Theme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        TaskNotificationChannel.create(this)
        
        enableEdgeToEdge()

        val database = AppDataBase.getDatabase(this)
        val appRepository = AppRepositoryImplementation(database.subjectDao(), database.taskDao())
        val authRepository = AuthRepositoryImplementation(FirebaseAuth.getInstance())
        val scheduler = TaskReminderSchedule(this)
        
        setContent {
            TodoAppV2Theme {
                val navController = rememberNavController()
                val authViewModel = remember { AuthViewModel(authRepository) }
                Scaffold(
                    bottomBar = { AppBottomBar(navController) }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        repository = appRepository,
                        schedule = scheduler
                    )
                }
            }
        }
    }
}
