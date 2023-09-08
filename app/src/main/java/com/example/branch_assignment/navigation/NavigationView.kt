package com.example.branch_assignment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.branch_assignment.ui.loading.LoadingScreen
import com.example.branch_assignment.ui.login.LoginScreen
import com.example.branch_assignment.ui.messages.conversation.ConversationScreen
import com.example.branch_assignment.ui.messages.threads.MessageThreadsScreen
import com.example.branch_assignment.ui.viewmodel.MainViewModel


@Composable
fun NavigationView(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    NavHost(
        startDestination = Destinations.LOADING,
        navController = navController
    ) {
        composable(route = Destinations.LOADING) {
            LoadingScreen(
                viewModel = viewModel,
                navigateToLogin = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.LOADING) {
                            inclusive = true
                        }
                    }
                },
                navigateToThreads = {
                    navController.navigate(Destinations.MESSAGE_THREADS) {
                        popUpTo(Destinations.LOADING) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Destinations.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { actions.openMessageThreads.invoke() })
        }
        composable(route = Destinations.MESSAGE_THREADS) {
            MessageThreadsScreen(
                viewModel = viewModel,
                navigateToConversation = { id ->
                    navController.navigate("${Destinations.CONVERSATION}/$id")
                })
        }
        composable(
            route = "${Destinations.CONVERSATION}/{${Destinations.ConversationArgs.THREAD_ID}}",
            arguments = listOf(navArgument(Destinations.ConversationArgs.THREAD_ID) {
                type = NavType.IntType
            })
        ) { backstackEntry ->

            ConversationScreen(
                threadId = backstackEntry.arguments?.getInt(Destinations.ConversationArgs.THREAD_ID)
                    ?: 0,
                viewModel = viewModel
            )
        }
    }
}


