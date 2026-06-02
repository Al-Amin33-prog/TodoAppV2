package com.example.todoappv2.auth.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthUiState

@Composable
fun LoginContent(
    isDarkMode: Boolean,
    onThemeChange:(Boolean) -> Unit,
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordIsVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        onThemeChange(isDarkMode)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isDarkMode)
                            R.drawable.brightness_7_24px
                            else
                            R.drawable.dark_mode_24px
                        ),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Theme",


                    )
                }
            }
            Text(
                text = stringResource(R.string.login_welcome),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                shape = RoundedCornerShape(16.dp),
                value = email,
                placeholder = {Text(stringResource(R.string.example_email))},
                onValueChange = {
                    email = it
                    if (state.error != null){
                        onEvent(AuthEvent.ClearError)
                    }
                                },

                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mail_24px),
                        contentDescription = null
                    )
                },
                label = { 
                    Text(
                        text = stringResource(R.string.email_label), 
                        style = MaterialTheme.typography.labelMedium
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.lock_24px),
                        contentDescription = "Lock"
                    )
                },
                shape = RoundedCornerShape(16.dp),
                value = password,
                onValueChange = { password = it },
                placeholder = {Text(stringResource(R.string._required))},
                label = { 
                    Text(
                        text = stringResource(R.string.password_label), 
                        style = MaterialTheme.typography.labelMedium
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordIsVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordIsVisible = !passwordIsVisible }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordIsVisible) R.drawable.visibility_off_24px
                                else R.drawable.visibility_24px
                            ),
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onForgotPassword) {
                    Text(
                        text = stringResource(R.string.forgot_password), 
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onEvent(
                        AuthEvent.Login(
                            email = email.trim(),
                            password = password.trim()
                        )
                    )
                },
                shape = RoundedCornerShape(24.dp),
                enabled = !state.isLoading,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNavigateToRegister,
                ) {
                    Text(
                        text = stringResource(R.string.register_prompt),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }



            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                   colors = CardDefaults.cardColors(
                       containerColor = MaterialTheme.colorScheme.errorContainer
                   ) ,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
