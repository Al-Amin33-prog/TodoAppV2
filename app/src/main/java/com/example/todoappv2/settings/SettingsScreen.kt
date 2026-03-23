package com.example.todoappv2.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R
import com.example.todoappv2.core.components.AppTopBar
import com.example.todoappv2.settings.components.AboutRow
import com.example.todoappv2.settings.components.HelpAndFeedbackRow
import com.example.todoappv2.settings.components.LogOutRow
import com.example.todoappv2.settings.components.SyncToggleRow
import com.example.todoappv2.settings.components.ThemeSelectorRow
import com.example.todoappv2.ui.theme.TodoAppV2Theme

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var isSyncEnabled by remember { mutableStateOf(true) }

    val accountSettings = listOf(
        SettingItem(R.string.profile, R.string.profile_desc,R.drawable.person_24px),
        SettingItem(R.string.security,R.string.security_desc,R.drawable.security_24px)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            AppTopBar(
               title = {
                   Text(
                       text = stringResource(R.string.settings_title),
                       style = MaterialTheme.typography.titleLarge,
                       color = MaterialTheme.colorScheme.primary
                   )
               },

                showBackButton = true,
                onBackClick = onBack,
                onSettingClick = {}
            )
        }


        item {
            SectionHeader(R.string.section_account)
        }
        items(accountSettings) { item ->
            SettingRow(item, onClick = {
             item.route?.let {

             }
            }
            )
        }

        item { SectionHeader(R.string.section_preferences) }
        item { 
            ThemeSelectorRow(
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange
            )
        }
        item {
            SyncToggleRow(
                isSyncEnabled = isSyncEnabled,
                onToggleSync = { isSyncEnabled = it }
            )
        }

        item { SectionHeader(R.string.section_other) }
        item { HelpAndFeedbackRow() }
        item { AboutRow() }
        item { 
            LogOutRow(onLogoutClick = onLogout)
        }
    }
}

@Composable
fun SectionHeader(titleRes: Int) {
    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TodoAppV2Theme {
        SettingsScreen(
            isDarkMode = true,
            onBack = {},
            onThemeChange = {},
            onLogout = {}
        ) 

    }
}
