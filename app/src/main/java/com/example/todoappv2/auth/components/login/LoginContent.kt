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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthUiState
import com.example.todoappv2.R

@Composable
fun LoginContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordIsVisible by remember{mutableStateOf(false)}
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn){
            onLoginSuccess()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff5f6fa))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "We've missed you ,Please sign in to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )


            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                shape = RoundedCornerShape(16.dp),
                value = email,
                onValueChange = {email = it},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mail_24px),
                        contentDescription = null
                    )
                },
                label = {Text("Email", style= MaterialTheme.typography.labelMedium)},
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
                onValueChange = {password = it},
                label = {Text("Password", style= MaterialTheme.typography.labelMedium)},

                modifier = Modifier.fillMaxWidth(),
                singleLine = true,



                visualTransformation = if (passwordIsVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {passwordIsVisible = !passwordIsVisible}
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordIsVisible)R.drawable.visibility_off_24px
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
                    Text("Forgot Password?", style = MaterialTheme
                        .typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier
                            .padding(8.dp)


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
                Text("Login",style = MaterialTheme.typography.labelMedium)
            }

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register",style = MaterialTheme.typography.bodyMedium )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

}