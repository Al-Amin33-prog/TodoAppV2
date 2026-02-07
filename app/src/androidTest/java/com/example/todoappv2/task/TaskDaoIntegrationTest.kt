package com.example.todoappv2.task


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.database.AppDataBase
import com.example.todoappv2.data.local.entity.TaskEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class TaskDaoIntegrationTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup(){
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        taskDao = dataBase.taskDao()
    }
    @After
    fun tearDown(){
        dataBase.close()
    }
    @Test
    fun insertTask_andFetchBySubject_returnTasks() = runTest {
        val task = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "Study Kotlin",
            description = null,
            createdAt = System.currentTimeMillis()
        )
        taskDao.insertTask(task)
        val tasks = taskDao.getTasksBySubject(10L).first()
        assertEquals(1,tasks.size)
        assertEquals("Study Kotlin",tasks.first().title)
    }
    @Test
    fun updateTask_updatesStoredTask() = runTest {
        val task = TaskEntity(
            id = 2L,
            subjectId = 10L,
            title = "Old Title",
            createdAt = System.currentTimeMillis()
        )
        taskDao.insertTask(task)
        val updated = task.copy(title = "New Title")
        taskDao.updateTask(updated)
        val fetched = taskDao.getTaskById(2L)
        assertEquals("New Title", fetched?.title)
    }
    @Test
    fun deleteTask_removesTask() = runTest {
        val task = TaskEntity(
            3L,
            10L,
            "Delete Me",
            createdAt = System.currentTimeMillis()
        )
        taskDao.insertTask(task)
        taskDao.deleteTask(task)
        val tasks = taskDao.getTasksBySubject(10L).first()
        assertTrue(tasks.isEmpty())
    }
}