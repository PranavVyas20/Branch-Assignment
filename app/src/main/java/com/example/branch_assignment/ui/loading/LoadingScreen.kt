package com.example.branch_assignment.ui.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.branch_assignment.ui.viewmodel.MainViewModel

@Composable
fun LoadingScreen(
    viewModel: MainViewModel, navigateToLogin: () -> Unit, navigateToThreads: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        val token = viewModel.getToken()
        if (token.isNullOrEmpty()) {
            navigateToLogin()
        } else {
            navigateToThreads()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column{
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .background(Color.LightGray)
            )
            Text(text = "Sample loading screen")
        }
        
    }
}