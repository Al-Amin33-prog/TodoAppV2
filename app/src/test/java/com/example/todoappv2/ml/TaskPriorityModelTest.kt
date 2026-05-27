package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TaskPriorityModelTest {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var model: TaskPriorityModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        model = TaskPriorityModel(mockContext)
    }

    @Test
    fun testFeatureExtraction() {
        val task = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "URGENT: Fix critical bug ASAP",
            description = "This is a critical issue",
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000, // 1 day from now
            isCompleted = false
        )

        val features = model.extractFeatures(task, 0.8f)

        assertEquals(true, features.hasKeywords)
        assertEquals(1, features.daysToDeadline)
        assertEquals(false, features.isOverdue)
        assertNotNull(features.titleLength)
    }

    @Test
    fun testPriorityPredictionWithoutTraining() {
        val task = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "Sample task",
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        )

        // Without training data, should return Medium priority
        val prediction = model.predictPriority(task)
        assertEquals(TaskPriorityModel.PriorityLevel.MEDIUM, prediction)
    }

    @Test
    fun testModelTraining() {
        // Create training tasks
        val urgentTask = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "URGENT: Fix bug ASAP",
            dueDate = System.currentTimeMillis() + 1 * 60 * 60 * 1000 // 1 hour
        )

        val normalTask = TaskEntity(
            id = 2L,
            subjectId = 10L,
            title = "Regular task",
            dueDate = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000 // 30 days
        )

        // Train model
        model.train(urgentTask, TaskPriorityModel.PriorityLevel.URGENT.value)
        model.train(normalTask, TaskPriorityModel.PriorityLevel.MEDIUM.value)

        // Verify model is trained
        val stats = model.getModelStats()
        assertEquals(2, stats["totalTrainingSamples"])
    }

    @Test
    fun testPredictionConfidence() {
        // Train model first
        val task1 = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "High priority task",
            dueDate = System.currentTimeMillis() + 1 * 60 * 60 * 1000
        )
        model.train(task1, TaskPriorityModel.PriorityLevel.HIGH.value)

        // Test prediction confidence
        val task2 = TaskEntity(
            id = 2L,
            subjectId = 10L,
            title = "Another high priority task",
            dueDate = System.currentTimeMillis() + 2 * 60 * 60 * 1000
        )
        
        val confidence = model.getPredictionConfidence(task2)
        assertEquals(true, confidence > 0.3f && confidence <= 1.0f)
    }

    @Test
    fun testUrgencyKeywordDetection() {
        val urgentTask = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "ASAP: Critical fix needed"
        )
        val features = model.extractFeatures(urgentTask)
        assertEquals(true, features.hasKeywords)

        val normalTask = TaskEntity(
            id = 2L,
            subjectId = 10L,
            title = "Read the documentation"
        )
        val normalFeatures = model.extractFeatures(normalTask)
        assertEquals(false, normalFeatures.hasKeywords)
    }

    @Test
    fun testOverdueTaskDetection() {
        val overdueTask = TaskEntity(
            id = 1L,
            subjectId = 10L,
            title = "Overdue task",
            dueDate = System.currentTimeMillis() - 1 * 60 * 60 * 1000 // 1 hour ago
        )
        val features = model.extractFeatures(overdueTask)
        assertEquals(true, features.isOverdue)
    }
}
