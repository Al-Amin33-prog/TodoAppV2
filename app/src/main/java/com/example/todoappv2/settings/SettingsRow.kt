package com.example.todoappv2.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SettingRow(item: SettingItem, navController: NavController) {
    ListItem(
        modifier = Modifier.clickable {
            item.route?.let { navController.navigate(it) }
        },
        headlineContent = {
            Text(
                item.title,
                color = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = { Text(item.subtitle) },
        leadingContent = {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
}
