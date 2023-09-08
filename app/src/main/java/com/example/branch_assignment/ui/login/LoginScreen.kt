package com.example.branch_assignment.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.branch_assignment.ui.viewmodel.MainViewModel
import com.example.branch_assignment.ui.viewmodel.MainViewModel.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: MainViewModel, onLoginSuccess: () -> Unit) {
    val loginState = viewModel.loginUiState.collectAsStateWithLifecycle().value
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var supportUsernameText by remember {
        mutableStateOf("")
    }
    var showLoader by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    when (loginState) {
        is UIState.Error -> {
            showLoader = false
            LaunchedEffect(key1 = true) {
                Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
            }
        }

        is UIState.Loading -> {
            showLoader= true
        }

        is UIState.Success -> {
            LaunchedEffect(key1 = true) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
        }

        is UIState.Initialised -> {}
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(supportingText = {
                Text(text = supportUsernameText, color = Color.Red)
            },
                label = { Text(text = "Enter username") },
                value = username,
                onValueChange = {
                    username = it
                    supportUsernameText = when {
                        username.matches(viewModel.emailRegex) -> ""
                        else -> "Invalid Username"
                    }
                })
            TextField(label = { Text(text = "Enter password") },
                value = password,
                onValueChange = { password = it })
            Button(onClick = {
                viewModel.validateAndLogin(username, password)
            }) {
                Text(text = "Login")
            }
        }
        if(showLoader) {
            CircularProgressIndicator()
        }
    }
}