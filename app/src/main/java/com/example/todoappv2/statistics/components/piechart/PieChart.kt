package com.example.todoappv2.statistics.components.piechart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.todoappv2.ui.theme.BluePrimary

@Composable
fun PieChart(
    completed: Int,
    pending: Int,
    overdue: Int
){

    val total = completed + pending + overdue

    if (total == 0)
        return
    val completedAngle = (completed / total.toFloat()) * 360f
    val pendingAngle = (pending / total.toFloat()) * 360f
    val overdueAngle = (overdue / total.toFloat()) * 360f

    Canvas(
        modifier = Modifier
            .size(140.dp)
    ) {
        var startAngle = -90f
        drawArc(
            color = Color(0xFF66BB6A),
            startAngle = startAngle,
            sweepAngle = completedAngle,
            useCenter = false,
            style = Stroke(width = 32f, cap = StrokeCap.Round)
        )
        startAngle += completedAngle

        drawArc(
            color = BluePrimary,
            startAngle = startAngle,
            sweepAngle = pendingAngle,
            useCenter = false,
            style = Stroke(width =32f, cap = StrokeCap.Round)
        )
        startAngle += pendingAngle

        drawArc(
            color = Color(0xffff9800),
            startAngle = startAngle,
            sweepAngle = overdueAngle,
            useCenter = false,
            style = Stroke(width = 32f, cap = StrokeCap.Round)
        )
    }
}