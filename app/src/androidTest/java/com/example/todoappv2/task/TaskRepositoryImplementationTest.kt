package com.example.todoappv2.task

import com.example.todoappv2.data.FakeAppRepository

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplementationTest {
   private lateinit var repository: AppRepository
   @Before
   fun setup(){
       repository = FakeAppRepository()
   }
    @Test
    fun insertTask_updateTaskFlow() = runTest {
        val task = TaskEntity(
            id = 1L,
            subjectId = 1L,
            title = "Repository Task",
            createdAt = System.currentTimeMillis()
        )
        repository.insertTask(task)
        val tasks = repository.getTasKBySubject(99L).first()
        assertEquals(1, tasks.size)
        assertEquals("Repository Task",tasks.first().title)

    }
    @Test
    fun updateTask_updateTaskFlow() = runTest {
        val task = TaskEntity(
            id = 2L,
            subjectId = 99L,
            "Old Task",
            createdAt = System.currentTimeMillis()
        )
        repository.insertTask(task)
        repository.updateTask(task.copy(title = "Updated Task"))
        val tasks = repository.getTasKBySubject(99L).first()
        assertEquals("Updated Task", tasks.first().title)
    }
    @Test
    fun deleteTask_removesTaskFromFlow() = runTest{
        val task = TaskEntity(
            id = 3L,
            subjectId = 99L,
            title = "To Delete",
            createdAt = System.currentTimeMillis()
        )
        repository.insertTask(task)
        repository.deleteTask(task)
        val tasks = repository.getTasKBySubject(99L).first()
        assertTrue(tasks.isEmpty())
    }

}