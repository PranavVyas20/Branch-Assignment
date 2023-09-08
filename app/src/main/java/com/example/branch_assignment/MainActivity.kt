package com.example.branch_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.branch_assignment.navigation.NavigationView
import com.example.branch_assignment.ui.theme.BranchAssignmentTheme
import com.example.branch_assignment.ui.viewmodel.MainViewModel
import com.example.branch_assignment.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    @Inject
     lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel by viewModels()
            BranchAssignmentTheme {
                NavigationView(viewModel = viewModel)
            }
        }
    }
}
