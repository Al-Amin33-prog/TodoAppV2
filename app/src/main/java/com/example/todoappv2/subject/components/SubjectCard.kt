package com.example.todoappv2.subject.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.example.todoappv2.data.local.entity.SubjectEntity

@Composable
fun SubjectCard(
    subject: SubjectEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable{onClick()}
    ) {
        Row(
           modifier = Modifier
               .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
               modifier = Modifier
                   .weight(1f)
            ) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                   painter = painterResource(R.drawable.ic_delete_24px),
                    contentDescription = "Delete Subject"
                )
            }
        }
    }
}