package com.example.todoappv2.task.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
    LazyRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(selected = selectedFilter
                    is TaskFilterType.All,
                onClick = {
                    onFilterSelected(TaskFilterType.All)
                },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BluePrimary,
                    selectedLabelColor = Color.Gray
                )
            )
        }

       item {
           FilterChip(
               selected = selectedFilter
                       is TaskFilterType.Pending,
               onClick = {
                   onFilterSelected(TaskFilterType.Pending)
               },
               label = {Text("Pending")},
               colors = FilterChipDefaults.filterChipColors(
                   selectedContainerColor = BluePrimary,
                   selectedLabelColor = Color.Gray
               )
           )
       }

     item {
         FilterChip(
             selected = selectedFilter
                     is TaskFilterType.Completed,
             onClick = {
                 onFilterSelected(TaskFilterType.Completed)
             },
             label = {Text("Completed")},
             colors = FilterChipDefaults.filterChipColors(
                 selectedContainerColor = BluePrimary,
                 selectedLabelColor = Color.Gray
             )
         )
     }

        item {
            FilterChip(
                selected = selectedFilter
                        is
                        TaskFilterType.Overdue,
                onClick = {
                    onFilterSelected(TaskFilterType.Overdue)
                },
                label = {Text("Overdue")},
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BluePrimary,
                    selectedLabelColor = Color.Gray
                )
            )
        }

    }

}