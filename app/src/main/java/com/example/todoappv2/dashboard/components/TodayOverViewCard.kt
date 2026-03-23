package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayOverviewCard(
    todayCount: Int,
    overDueCount: Int
){

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Today's Overview ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
               OverViewItemCard(
                   title = "Today",
                   count = todayCount,
                   modifier = Modifier.weight(1f)
               )
                OverViewItemCard(
                    title = "Overdue",
                    count = overDueCount,
                    isError = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }

}
