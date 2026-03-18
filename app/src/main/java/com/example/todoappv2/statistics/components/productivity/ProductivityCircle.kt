package com.example.todoappv2.statistics.components.productivity

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoappv2.ui.theme.BluePrimary

@Composable
fun ProductivityCircle(
    progress: Int
){
    val sweepAngle = (progress / 100f) * 360f
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ){
            drawArc(
              color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 10f)

            )
            drawArc(
                color = BluePrimary,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$progress",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}