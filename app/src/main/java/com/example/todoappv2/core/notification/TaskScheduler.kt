package com.example.todoappv2.core.notification

import com.example.todoappv2.data.local.entity.TaskEntity

interface TaskScheduler {
    fun schedule(task: TaskEntity)
    fun cancel(taskId: Long)
}
