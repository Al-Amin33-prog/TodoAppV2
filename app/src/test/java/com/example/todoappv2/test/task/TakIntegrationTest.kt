package com.example.todoappv2.test.task


import com.example.todoappv2.task.TaskEvent
import com.example.todoappv2.task.TaskViewModel
import com.example.todoappv2.test.data.repository.FakeAppRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TakIntegrationTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: TaskViewModel
    @Before
    fun setUp(){
        repository = FakeAppRepository()
        viewModel = TaskViewModel(repository,1L)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun adding_task_updates_visible_tasks()= runTest {
        viewModel.onEvent(
            TaskEvent.AddTask(
                1L,
                "Study calculus",
                null,
                null
            )
        )
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(1,state.visibleTasks.size)
        assertEquals("Study calculus", state.visibleTasks.first().title)
    }
}