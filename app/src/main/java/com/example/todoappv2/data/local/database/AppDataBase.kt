package com.example.todoappv2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoappv2.data.local.dao.SubjectDao
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity

@Database(
    entities = [
        SubjectEntity::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false

)
abstract class AppDataBase : RoomDatabase(){
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
}