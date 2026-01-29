package com.example.todoappv2.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todoappv2.core.receiver.TaskReminderReceiver

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleTaskReminder(taskId: Long, title: String, description: String?, timeInMillis: Long) {
        // 1. Check if we can schedule exact alarms (Required for Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("AlarmHelper", "Cannot schedule exact alarm: Permission revoked by user")
                // Ideally, you'd show a UI message here asking the user to grant permission in settings
                return
            }
        }

        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra("TASK_ID", taskId)
            putExtra("TASK_TITLE", title)
            putExtra("TASK_DESCRIPTION", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Log.e("AlarmHelper", "SecurityException: Failed to schedule exact alarm", e)
        }
    }

    fun cancelTaskReminder(taskId: Long) {
        val intent = Intent(context, TaskReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
