package com.example.todoappv2.subject.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.SubjectEntity

@Composable
fun SubjectList(
    subjects: List<SubjectEntity>,
    onDelete: (SubjectEntity) -> Unit,
    onSubjectClick: (SubjectEntity) -> Unit,
    onAddSubject: () -> Unit,
    onEditSubject: (SubjectEntity) -> Unit,
    isSelectionMode: Boolean,
    selectedSubjectId: Set<Long>,
    onToggleSelectionMode: () -> Unit,
    onToggleSelection: (Long) -> Unit

){
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(subjects, key = {it.id}){subject ->
            SubjectCard(
                subject = subject,
                onClick = {onSubjectClick(subject)},
                onDelete = {onDelete(subject)},
                onEdit = {onEditSubject(subject)},
                isSelected = selectedSubjectId.contains(subject.id),
                isSelectionMode = isSelectionMode,
                onLongPress = {
                    onToggleSelectionMode()
                    onToggleSelection(subject.id)
                },
                onSelect = {
                    onToggleSelection(subject.id)
                }

            )

        }

    }

}