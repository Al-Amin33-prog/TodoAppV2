package com.example.todoappv2.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R

@Composable
fun DeleteBackground(dismissState: SwipeToDismissBoxState){



    val progress = dismissState.progress.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)
        .background(MaterialTheme.colorScheme.error.copy(alpha = progress)),
        contentAlignment = Alignment.CenterEnd

    ){
        Icon(
            painter = painterResource(R.drawable.ic_delete_24px),
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.graphicsLayer{
                translationX = 100f *(1-progress)
                alpha = progress
            }
        )

    }





}
