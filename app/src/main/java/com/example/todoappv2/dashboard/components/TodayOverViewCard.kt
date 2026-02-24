package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Today's Overview ",
                style = MaterialTheme
                    .typography.titleMedium,

            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Today")
                    Text(
                        text = todayCount.toString(),
                        style  = MaterialTheme.typography.headlineMedium
                    )
                }
                Column {
                    Text("Overdue")
                    Text(
                        text = overDueCount.toString(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}
