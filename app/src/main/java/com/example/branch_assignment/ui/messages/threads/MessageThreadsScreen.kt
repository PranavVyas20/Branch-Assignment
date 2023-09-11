package com.example.branch_assignment.ui.messages.threads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.branch_assignment.data.model.message.MessageDto
import com.example.branch_assignment.data.model.message.MessageThread
import com.example.branch_assignment.ui.theme.Purple40
import com.example.branch_assignment.ui.viewmodel.MainViewModel

@Composable
fun MessageThreadsScreen(viewModel: MainViewModel, navigateToConversation: (Int) -> Unit) {
    val messageThreadsState = viewModel.messagesThreadsUiState.collectAsStateWithLifecycle().value
    LaunchedEffect(key1 = true) {
        if (messageThreadsState is MainViewModel.UIState.Loading) {
            viewModel.getMessagesThreads()
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        when (messageThreadsState) {
            is MainViewModel.UIState.Error -> {
                Text("Error fetching data")
            }

            is MainViewModel.UIState.Initialised -> {}

            is MainViewModel.UIState.Loading -> {
                CircularProgressIndicator()
            }

            is MainViewModel.UIState.Success -> {
                MessageThreadsScreenContent(
                    threads = messageThreadsState.data,
                    onMessageThreadClick = { navigateToConversation(it) })
            }
        }
    }
}

@Composable
fun MessageThreadsScreenContent(
    threads: List<MessageThread>,
    onMessageThreadClick: (threadId: Int) -> Unit
) {
    val lazyColumnState = rememberLazyListState()
    val messagesToDisplay = remember(threads) {
        threads.map { it.messages.last() }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Message Threads",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(messagesToDisplay) { message ->
                MessageThreadItemView(message = message, onClick = { threadId ->
                    onMessageThreadClick(threadId)
                })
            }
        }
    }

}

@Composable
fun MessageThreadItemView(message: MessageDto, onClick: (threadId: Int) -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(message.threadId)
                }
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextItem(heading = "Id", value = message.id.toString())

            TextItem(
                heading = "Sender Id",
                value = if (message.agentId.isNullOrEmpty()) message.userId else message.agentId)
            TextItem(heading = "Thread Id", value = message.threadId.toString())
            TextItem(heading = "Created at", value = message.dateTime)
            TextItem(heading = "Message", value = message.messageBody)
            Text(
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Open Conversation",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Purple40, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun TextItem(heading: String, value: String) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("$heading: ")
        }
        append(value)
    }
    Text(text = text, maxLines = 3, overflow = TextOverflow.Ellipsis)
}

@Preview
@Composable
fun MessageThreadPreview() {
    MessageThreadItemView(
        message = MessageDto(
            id = 1,
            threadId = 33,
            userId = "user-id",
            agentId = "agent-id",
            dateTime = "209382000",
            messageBody = "Sample message body here!",
        )
    ) {}
}