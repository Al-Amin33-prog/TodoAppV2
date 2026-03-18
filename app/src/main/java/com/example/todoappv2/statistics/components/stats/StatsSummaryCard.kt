package com.example.todoappv2.statistics.components.stats



import com.example.todoappv2.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatsSummaryCards(
    total: Int,
    completed: Int,
    pending : Int
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
       horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatsCard(
            title = "Total Tasks",
            value = total,
            icon = R.drawable.list_24px,
            iconColor = Color(0xFF4A90E2),
            modifier = Modifier.weight(1f)
            )
        StatsCard(
            title = "Completed",
            value = completed,
            icon = R.drawable.check_circle_24px,
            iconColor = Color(0xFF66BB6A),
            modifier = Modifier.weight(1f))
        StatsCard(title =  "Pending",
            value = pending,
            icon = R.drawable.pending_actions_24px,
            iconColor = Color(0xFFFFA726),
            modifier = Modifier.weight(1f)


            )
    }
}
