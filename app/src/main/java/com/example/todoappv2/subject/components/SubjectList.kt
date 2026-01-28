package com.example.todoappv2.subject.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.todoappv2.data.local.entity.SubjectEntity

@Composable
fun SubjectList(
    subjects: List<SubjectEntity>,
    onDelete: (SubjectEntity) -> Unit,
    onSubjectClick: (SubjectEntity) -> Unit = {}
){
    LazyColumn {
        items(subjects){subject ->
            SubjectCard(
                subject = subject,
                onClick = {onSubjectClick(subject)},
                onDelete = {onDelete(subject)}
            )
        }
    }

}