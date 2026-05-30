package com.example.todoappv2.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todoappv2.data.local.entity.TaskEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidTaskScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(task: TaskEntity) {
        if (task.dueDate == null) return

        // Android 12+ Exact Alarm check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }

        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra(EXTRA_TASK_TITLE, task.title)
            putExtra(EXTRA_TASK_ID, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            task.dueDate,
            pendingIntent
        )
    }

    override fun cancel(taskId: Long) {
        val intent = Intent(context, TaskReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}
