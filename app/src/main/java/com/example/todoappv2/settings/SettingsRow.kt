package com.example.todoappv2.settings

import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource


@Composable
fun SettingRow(item: SettingItem,
               onClick: () -> Unit) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.clickable{onClick()}

        ,
        headlineContent = {
            Text(
                text = stringResource(item.titleRes),

                style = MaterialTheme.typography.labelLarge,
                color = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = { Text(
          text = stringResource(item.subtitleRes)
        ) },
        leadingContent = {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    )
    HorizontalDivider()
}
