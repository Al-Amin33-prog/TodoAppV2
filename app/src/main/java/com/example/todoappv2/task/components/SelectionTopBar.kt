package com.example.todoappv2.task.components

import com.example.todoappv2.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onDeleteSelected:  () -> Unit,
    isAllSelected: Boolean,
    onSelectAll: () -> Unit
){
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            title = {
                Text(
                    "$selectedCount selected"
                )
            },
            navigationIcon = {
                IconButton(onClick = onClearSelection) {
                    Icon(
                        painter = painterResource(R.drawable.close_small_24px),
                        contentDescription = "Close"
                    )
                }
            },
            actions = {
                IconButton(onClick = onSelectAll) {
                    Icon(
                        painter = painterResource(
                            if (isAllSelected) R.drawable.ic_delete_24px
                            else R.drawable.select_all_24px
                        ),
                        contentDescription = "Select All"
                    )
                }
                IconButton(onClick = onDeleteSelected) {
                    Icon(
                        painter =painterResource(R.drawable.ic_delete_24px),
                        contentDescription = "Delete"
                    )
                }

            }
        )
    }
}
