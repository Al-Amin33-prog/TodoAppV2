package com.example.todoappv2.subject

import com.example.todoappv2.data.local.entity.SubjectEntity


sealed class SubjectEvent{
    data class AddSubject(val name: String): SubjectEvent()
    data class DeleteSubject(val subject: SubjectEntity): SubjectEvent()
    data class UpdateSubject(val subject: SubjectEntity): SubjectEvent()
}