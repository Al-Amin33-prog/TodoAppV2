package com.example.todoappv2.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class TaskReminderReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
       val title = intent.getStringExtra("task_title")?: "Task Reminder"
        NotificationHelper.showTaskNotification(
            context = context,
            title = "Reminder",
            message = title

        )
    }
}
