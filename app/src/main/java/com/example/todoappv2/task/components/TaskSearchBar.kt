package com.example.todoappv2.task.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R

@Composable
fun TaskSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
){
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_24px__1_),
                contentDescription = null
            )
        },
        singleLine = true
    )
}