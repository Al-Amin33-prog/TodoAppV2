package com.example.todoappv2.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoappv2.data.local.dao.PriorityTrainingDao
import com.example.todoappv2.data.local.dao.SubjectDao
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.local.entity.PriorityTrainingEntity

@Database(
    entities = [
        SubjectEntity::class,
        TaskEntity::class,
        PriorityTrainingEntity::class
    ],
    version = 4,
    exportSchema = false

)

abstract class AppDataBase : RoomDatabase(){

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
    abstract fun priorityTrainingDao(): PriorityTrainingDao

    companion object {
        private val MIGRATION_1_TO_2 =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "ALTER TABLE tasks ADD COLUMN priority TEXT NOT NULL DEFAULT 'Medium'"
                    )
                }
            }

        private val MIGRATION_3_TO_4 =
            object : Migration(3, 4) {
                override fun migrate(db: SupportSQLiteDatabase) {

                    db.execSQL(
                        """
                CREATE TABLE IF NOT EXISTS priority_training (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    taskTitle TEXT NOT NULL,
                    subjectId INTEGER NOT NULL,
                    dueDate INTEGER,
                    isCompleted INTEGER NOT NULL,
                    createdAt INTEGER NOT NULL,
                    priorityLevel INTEGER NOT NULL
                )
                """
                    )
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
                    .addMigrations(
                        MIGRATION_1_TO_2,
                        MIGRATION_3_TO_4
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

}
}