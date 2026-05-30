package com.example.todoappv2.ml.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PriorityBadge(
    priority: String,
    confidence: Float,
    modifier: Modifier = Modifier
){
  val priorityColor = when(priority.lowercase()){
      "urgent" -> Color(0xcffb71c1c)
      "high" -> Color(0xfff57c00)
      "medium" -> Color(0xfff9a825)
      "low" -> Color(0xff388e3c)
      else -> Color.Gray
  }
    val confidencePercent = (confidence * 100).toInt()
    Surface(
        color = priorityColor.copy(alpha = 0.3f),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = "$priority\n$confidencePercent%",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                color = priorityColor
            ),
            modifier = Modifier.padding(4.dp)
        )
    }
}