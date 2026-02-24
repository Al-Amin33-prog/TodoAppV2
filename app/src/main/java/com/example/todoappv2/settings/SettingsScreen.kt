package com.example.todoappv2.settings


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoappv2.R
import com.example.todoappv2.core.navigation.Routes
import com.example.todoappv2.settings.components.AboutRow
import com.example.todoappv2.settings.components.HelpAndFeedbackRow
import com.example.todoappv2.settings.components.LogOutRow
import com.example.todoappv2.settings.components.SyncToggleRow
import com.example.todoappv2.settings.components.ThemeSelectorRow
import com.example.todoappv2.ui.theme.TodoAppV2Theme

@Composable
fun SettingsScreen(
    appNavController: NavController,
   navController: NavController,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var isSyncEnabled by remember { mutableStateOf(true) }

    val accountSettings = listOf(
        SettingItem("Profile", "View and edit your personal info", R.drawable.person_24px),
        SettingItem("Security", "Passwords and 2FA", R.drawable.security_24px)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item { SectionHeader("Account") }
        items(accountSettings) { item -> SettingRow(item, appNavController) }

        item { SectionHeader("Preferences") }
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

        item { SectionHeader("Other") }
        item { HelpAndFeedbackRow() }
        item { AboutRow() }
        item { 
            LogOutRow(onLogoutClick = {
              navController.navigate(Routes.LOGOUT)
            }) 
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
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
            appNavController = rememberNavController(),
            isDarkMode = false,
            onThemeChange = {},
           navController = rememberNavController(),
            onLogout = {}
        )
    }
}
