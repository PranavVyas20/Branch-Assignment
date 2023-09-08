package com.example.branch_assignment.ui.messages.conversation


import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.branch_assignment.data.model.message.MessageDto
import com.example.branch_assignment.ui.messages.threads.TextItem
import com.example.branch_assignment.ui.viewmodel.MainViewModel


@Composable
fun ConversationScreen(threadId: Int, viewModel: MainViewModel) {
    val conversationState = viewModel.conversationUiState.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = true) {
        viewModel.getConversationFromThread(threadId)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (conversationState) {
            is MainViewModel.UIState.Error -> {
            }

            is MainViewModel.UIState.Initialised -> {
            }

            is MainViewModel.UIState.Loading -> {
                CircularProgressIndicator()
            }

            is MainViewModel.UIState.Success -> {
                ConversationScreenContent(
                    messages = conversationState.data,
                    onClickSendMessage = { body ->
                        viewModel.postMessage(threadId, body)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ConversationScreenContent(
    messages: List<MessageDto>,
    onClickSendMessage: (messageBody: String) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = messages) {
        listState.scrollToItem(messages.lastIndex)
    }
    var textFieldValue by remember { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (conversationList, messageTextField, headingTextField) = createRefs()
        Text(
            text = "Conversations",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(headingTextField) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 8.dp)
            })
        LazyColumn(
            state = listState,
            modifier = Modifier.constrainAs(conversationList) {
                top.linkTo(headingTextField.bottom)
                bottom.linkTo(messageTextField.top)
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = messages, key = { it.id }) { message ->
                ConversationItemView(message = message)
            }
        }
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .constrainAs(messageTextField) {
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                },
            shape = RoundedCornerShape(8.dp),
            value = textFieldValue,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            placeholder = { Text(text = "Type your message here") },
            onValueChange = { textFieldValue = it },
            trailingIcon = {
                IconButton(onClick = {
                    onClickSendMessage(textFieldValue)
                    textFieldValue = ""
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "send-icon")
                }
            }
        )
    }
}

@Composable
fun ConversationItemView(message: MessageDto) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextItem(
                heading = "Sender Id",
                value = (message.agentId ?: message.userId).toString()
            )
            TextItem(heading = "Message", value = message.messageBody)
            TextItem(heading = "Time", value = message.dateTime)
        }
    }

}

@Preview
@Composable
fun ConversationItemPreview() {
    ConversationItemView(
        message = MessageDto(
            id = 1,
            threadId = 33,
            userId = "user-id",
            agentId = "agent-id",
            dateTime = "209382000",
            messageBody = "Sample message body here!",
        )
    )

}
