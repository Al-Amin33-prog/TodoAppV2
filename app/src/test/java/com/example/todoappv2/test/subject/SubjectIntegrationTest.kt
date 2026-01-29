package com.example.todoappv2.test.subject

import com.example.todoappv2.subject.SubjectEvent
import com.example.todoappv2.subject.SubjectViewModel

import com.example.todoappv2.test.data.repository.FakeAppRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubjectIntegrationTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: SubjectViewModel
    @Before
    fun setUp(){
        repository = FakeAppRepository()
        viewModel = SubjectViewModel(repository)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun adding_subject_update_viewmodel_state()= runTest {
        viewModel.onEvent(
            SubjectEvent.AddSubject("Physics")
        )
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(1,state.subjects.size)
        assertEquals("Physics",state.subjects.first().name)
        assertEquals(false, state.isLoading)
    }

}