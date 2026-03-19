package com.example.todoappv2.statistics.components.barchart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyBarChart(
    values: List<Int>
){
    val days = listOf("Mon","Tue","Wed", "Thu","Fri","Sat","Sun")
    val maxValue = values.maxOrNull()?.coerceAtLeast(1)?:1
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface) ,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "Weekly Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom) {
                values.take(7).forEachIndexed { index, value ->
                    val barHeightFactor = value.toFloat()/maxValue
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,

                          ){
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .fillMaxHeight(barHeightFactor.coerceAtLeast(0.05f))
                                .background(color = Color(0xFF66BB6A),
                                    shape = RoundedCornerShape(topStart = 4.dp,
                                        topEnd = 4.dp
                                    )
                                )

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = days.getOrElse(index){""},
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray

                        )


                    }

                }
            }
        }

    }
}
