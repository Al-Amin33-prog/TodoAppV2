package com.example.todoappv2.test.data.repository

import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAppRepository: AppRepository {
    private val subjectsFlow = MutableStateFlow<List<SubjectEntity>>(emptyList())
    private val tasksFlow = MutableStateFlow<List<TaskEntity>>(emptyList())

    override fun getSubjects(): Flow<List<SubjectEntity>> = subjectsFlow

    override suspend fun insertSubject(subject: SubjectEntity) {
        subjectsFlow.value = subjectsFlow.value + subject
    }

    override suspend fun deleteSubject(subject: SubjectEntity) {
        subjectsFlow.value = subjectsFlow.value - subject
    }

    override suspend fun updateSubject(subject: SubjectEntity) {
        subjectsFlow.value = subjectsFlow.value.map {
            if (it.id == subject.id) subject else it
        }
    }

    override suspend fun getSubjectById(subjectId: Long?): SubjectEntity? {
        return subjectsFlow.value.firstOrNull { it.id == subjectId }
    }

    override suspend fun getTaskById(taskId: Long): TaskEntity? {
        return tasksFlow.value.firstOrNull { it.id == taskId }
    }

    override fun getTasKBySubject(subjectId: Long): Flow<List<TaskEntity>> =
        tasksFlow.map { tasks ->
            tasks.filter { it.subjectId == subjectId }
        }

    override suspend fun insertTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value + task
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value.map {
            if (it.id == task.id) task else it
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value - task
    }
}
