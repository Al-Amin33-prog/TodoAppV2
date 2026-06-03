package com.example.todoappv2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priority_training")
data class PriorityTrainingEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val taskTitle: String,

    val subjectId: Long,

    val dueDate: Long?,

    val isCompleted: Boolean,

    val createdAt: Long,

    val priorityLevel: Int
)