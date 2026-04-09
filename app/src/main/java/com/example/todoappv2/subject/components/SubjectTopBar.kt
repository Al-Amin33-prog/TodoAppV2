package com.example.todoappv2.subject.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R

@Composable
fun SubjectTopBar(
    isSelectionMode: Boolean,
    selectedCount: Int,
    totalCount: Int,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelected: () -> Unit
){
    AnimatedContent(
        targetState = isSelectionMode,
        transitionSpec = {
            slideInVertically { -it  } + fadeIn() togetherWith
                    slideOutVertically {it} + fadeOut()
        },
        label = "TopBarAnimation"
    ) { isSelecting ->
        if (isSelecting){
            Row(
                modifier = Modifier.
                fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp,
                        vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClearSelection) {
                    Icon(
                        painter = painterResource(R.drawable.close_small_24px),
                        contentDescription = "Close"
                    )
                }
                Text(
                    text = "$selectedCount selected",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onSelectAll) {
                    Icon(
                        painter = painterResource(R.drawable.select_all_24px),
                        contentDescription = "Select All"
                    )
                }
                IconButton(onClick = onDeleteSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_24px),
                        contentDescription = "Delete"
                    )
                }
            }
        }else{
            Text(
                text = "Subjects",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}