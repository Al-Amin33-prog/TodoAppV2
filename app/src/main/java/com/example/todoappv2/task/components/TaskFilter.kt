package com.example.todoappv2.task.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoappv2.ui.theme.BluePrimary


@Composable
fun TaskFilter(
    selectedFilter: TaskFilterType,
    onFilterSelected: (TaskFilterType) -> Unit
){
    val filters = listOf(
        TaskFilterType.All to "All",
        TaskFilterType.Pending to "Pending",
        TaskFilterType.Completed to "Completed",
        TaskFilterType.Overdue to "Overdue"
    )
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters){(type, label) ->
            FilterChip(
                selected = selectedFilter == type,
                onClick = {onFilterSelected(type)},
                label = {Text(label)},
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BluePrimary,
                    selectedLabelColor = Color.White,
                    containerColor = Color.Transparent,
                    labelColor = Color.Gray
                )
            )

        }

    }


}