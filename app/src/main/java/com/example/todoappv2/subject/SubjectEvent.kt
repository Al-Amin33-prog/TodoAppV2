package com.example.todoappv2.subject

import com.example.todoappv2.data.local.entity.SubjectEntity


sealed class SubjectEvent{
    data class AddSubject(val name: String): SubjectEvent()
    data class DeleteSubject(val subject: SubjectEntity): SubjectEvent()
    data class UpdateSubject(val subject: SubjectEntity): SubjectEvent()
    data class SearchQueryChange(val query: String): SubjectEvent()
    object ToggleSelectionMode: SubjectEvent()
    data class ToggleSubjectSelection(val subjectId: Long) : SubjectEvent()
    object ClearSelection:  SubjectEvent()
    object DelectedSelectedSubjects: SubjectEvent()
    object SelectAllSubjects: SubjectEvent()
}