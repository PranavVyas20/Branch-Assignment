package com.example.branch_assignment.navigation

import androidx.navigation.NavHostController

object Destinations {
    const val LOGIN = "login"
    const val MESSAGE_THREADS = "message_threads"
    const val CONVERSATION = "conversation"
    const val LOADING = "loading"

    object ConversationArgs {
        const val THREAD_ID = "threadId"
    }
}

class Actions(navController: NavHostController) {
    val openMessageThreads = {
        navController.navigate(Destinations.MESSAGE_THREADS) {
            popUpTo(Destinations.LOGIN){
                inclusive = true
            }
        }
    }
    val openConversation: (threadId: Int) -> Unit = { id->
        navController.navigate("${Destinations.CONVERSATION}/$id")
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}