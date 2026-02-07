package com.example.todoappv2.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todoappv2.data.local.entity.TaskEntity

class TaskReminderSchedule (
    private val context: Context
){
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleTaskReminder(task: TaskEntity){
        if (task.dueDate == null) return

        // Android 12+ Exact Alarm check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("TaskReminderSchedule", "Permission not granted to schedule exact alarms")
                return
            }
        }

        val intent = Intent(context, TaskReminderReceiver::class.java).apply{
            putExtra("task_title", task.title)
            putExtra("task_id", task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                task.dueDate,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Log.e("TaskReminderSchedule", "SecurityException while scheduling alarm", e)
        }
    }

    fun cancelTaskReminder(taskId: Long){
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
