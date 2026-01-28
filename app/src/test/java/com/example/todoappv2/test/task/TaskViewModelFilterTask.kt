package com.example.todoappv2.test.task

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.TaskEvent
import com.example.todoappv2.task.TaskViewModel
import com.example.todoappv2.task.components.TaskFilterType
import com.example.todoappv2.test.data.repository.FakeAppRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskViewModelFilterTask {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()
    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: TaskViewModel
    private val subjectId = 1L
    @Before
    fun setUp(){
        repository = FakeAppRepository()
        viewModel = TaskViewModel(
            repository = repository,
            subjectId = subjectId
        )
    }
    private fun addTasks() = runTest{
        repository.insertTask(
            TaskEntity(
                1,
                subjectId = subjectId,
                title = "Completed Task",
                isCompleted = true,
                createdAt = System.currentTimeMillis()
            )
        )

        repository.insertTask(
            TaskEntity(
                1,
                subjectId = subjectId,
                title = "pending Task",
                isCompleted = false,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    @Test
    fun allFilterShowsAllTasks() = runTest {
        addTasks()
        viewModel.onEvent(TaskEvent.ChangeFilter(TaskFilterType.ALL))
        val state = viewModel.uiState.first { !it.isLoading }
        assertEquals(2, state.visibleTasks.size)
    }
    @Test
    fun pendingFilterShowOnlyPendingTask() = runTest {
        addTasks()
        viewModel.onEvent(TaskEvent.ChangeFilter(TaskFilterType.PENDING))
    }
    @Test
    fun addTaskInsertTaskAndUpdateUiState() = runTest {
        viewModel.onEvent(
            TaskEvent.AddTask(
              subjectId =   subjectId,
                title = "New Task",
                description = "Test Description",
                dueDate = null

            )
        )
        val state = viewModel.uiState.first { !it.isLoading }
        assertEquals(1, state.allTasks.size)
        assertEquals("New task", state.allTasks.first().title)
    }
   @Test
   fun updateTaskModifiesExistingTask() = runTest {
       val task =  TaskEntity(
           1,
           subjectId = subjectId,
           title = "Completed Task",
           isCompleted = true,
           createdAt = System.currentTimeMillis()
       )
       repository.insertTask(task)
       viewModel.onEvent(
           TaskEvent.UpdateTask(
               task.copy(title = "Update Title")
           )
       )
       val state = viewModel.uiState.first { !it.isLoading }
       assertEquals("Update Title", state.allTasks.first().title)
   }



}