package com.example.todoappv2.subject.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
    isAllSelected: Boolean,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelected: () -> Unit,
    onDeselectedSubject: () -> Unit
){
    // This bar ONLY shows during selection mode. 
    // The "Subjects" title is handled by the AppNavigationShell.
    AnimatedVisibility(
        visible = isSelectionMode,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClearSelection) {
                Icon(
                    painter = painterResource(R.drawable.close_small_24px),
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(
                text = "$selectedCount selected",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            IconButton(onClick = {
                if (isAllSelected)
                    onDeselectedSubject()
                else onSelectAll()
            }) {
                Icon(

                    painter = painterResource(
                        if (isAllSelected)
                     R.drawable.select_all_24px
                        else R.drawable.deselect_24px
                    ),
                    contentDescription =
                        if (isAllSelected)
                        "DeSelect All"
                    else "Select All",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(onClick = onDeleteSelected) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_24px),
                    contentDescription = "Delete Selected",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
