package com.example.todoappv2.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoappv2.data.local.dao.SubjectDao
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity

@Database(
    entities = [
        SubjectEntity::class,
        TaskEntity::class
    ],
    version = 2,
    exportSchema = false

)

abstract class AppDataBase : RoomDatabase(){

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao

    companion object {
        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add the new priority column with default value "Medium"
                db.execSQL("ALTER TABLE tasks ADD COLUMN priority TEXT NOT NULL DEFAULT 'Medium'")
            }
        }

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "todo_database"
                )
                    .addMigrations(MIGRATION_1_TO_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

}
}