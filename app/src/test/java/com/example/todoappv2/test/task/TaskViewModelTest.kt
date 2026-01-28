package com.example.todoappv2.test.task

import com.example.todoappv2.task.TaskEvent
import com.example.todoappv2.task.TaskViewModel
import com.example.todoappv2.test.data.repository.FakeAppRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskViewModelTest {
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
    @Test
    fun addTaskUpdatesUiStateVisibleTasks() = runTest {
        viewModel.onEvent(
            TaskEvent.AddTask(
                subjectId = subjectId,
                title = "Test Task",
                description = "Test Description",
                dueDate = null
            )
        )
        val state = viewModel.uiState.first { !it.isLoading }
        assertEquals(1,state.visibleTasks.size)
        assertEquals("Test Task", state.visibleTasks.first().title)
    }
}