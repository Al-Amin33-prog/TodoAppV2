package com.example.todoappv2.test.subject


import com.example.todoappv2.subject.SubjectEvent
import com.example.todoappv2.subject.SubjectViewModel
import com.example.todoappv2.test.data.repository.FakeAppRepository
import com.example.todoappv2.test.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubjectViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var repository: FakeAppRepository
    private lateinit var viewModel: SubjectViewModel

    @Before
    fun setUp(){
        repository = FakeAppRepository()
        viewModel = SubjectViewModel(repository)
    }
    @Test
    fun addingSubjectUpdatesSubjectsList() = runTest {
        viewModel.onEvent(SubjectEvent.AddSubject("Mathematics"))

        val state = viewModel.uiState.value
        assertEquals(1, state.subjects.size)
        assertEquals("Mathematics",state.subjects.first().name)
    }
    fun deletingSubjectDeleteSubjectsList() = runTest {

    }
  }
