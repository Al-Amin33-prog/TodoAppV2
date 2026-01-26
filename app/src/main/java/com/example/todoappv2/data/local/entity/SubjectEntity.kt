package com.example.todoappv2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val colorHex: Long,
    val createdAt: Long =  System.currentTimeMillis()
)

