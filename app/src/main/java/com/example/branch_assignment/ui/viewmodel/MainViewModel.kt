package com.example.branch_assignment.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch_assignment.data.model.login.LoginResponse
import com.example.branch_assignment.data.model.message.MessageDto
import com.example.branch_assignment.data.model.message.MessageThread
import com.example.branch_assignment.repo.MainRepository
import com.example.branch_assignment.utils.NetworkResult
import com.example.branch_assignment.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val mainRepository: MainRepository
) : ViewModel() {

    sealed class UIState<T> {
        data class Success<T>(val data: T) : UIState<T>()

        data class Error<T>(val message: String?) : UIState<T>()

        class Loading<T> : UIState<T>()

        class Initialised<T> : UIState<T>()
    }

    private var messageThreadsMap: Map<Int, List<MessageDto>> = mapOf()
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    private val _loginUiState = MutableStateFlow<UIState<LoginResponse>>(UIState.Initialised())
    val loginUiState get() = _loginUiState

    private val _messagesThreadsUiState =
        MutableStateFlow<UIState<List<MessageThread>>>(UIState.Loading())
    val messagesThreadsUiState get() = _messagesThreadsUiState

    private val _conversationUiState =
        MutableStateFlow<UIState<List<MessageDto>>>(UIState.Loading())
    val conversationUiState get() = _conversationUiState

     fun validateAndLogin(username: String, password: String) {
        val isValidUsername = username.matches(emailRegex)
        if (isValidUsername) {
            loginUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            mainRepository.loginUser(username, password).collect { loginResponse ->
                when (loginResponse) {
                    is NetworkResult.Error -> {
                        _loginUiState.value = UIState.Error("Error logging in")
                    }

                    is NetworkResult.Loading -> {
                        _loginUiState.value = UIState.Loading()
                    }

                    is NetworkResult.Success -> {
                        tokenManager.saveToken(loginResponse.data!!.authToken)
                        _loginUiState.value = UIState.Success(loginResponse.data)
                    }
                }
            }
        }

    }

    fun getMessagesThreads(refreshMessages: Boolean = false, threadId: Int? = null) {
        viewModelScope.launch {
            mainRepository.getAllMessages().collect { messagesResponse ->
                when (messagesResponse) {
                    is NetworkResult.Error -> {
                        _messagesThreadsUiState.value =
                            UIState.Error(messagesResponse.message.toString())
                    }

                    is NetworkResult.Loading -> {
                        _messagesThreadsUiState.value = UIState.Loading()
                    }

                    is NetworkResult.Success -> {
                        messageThreadsMap = getMessageThreadMap(messagesResponse.data!!)
                        Log.d("thread-tag", messageThreadsMap.toString())

                        if (refreshMessages) {
                            getConversationFromThread(threadId!!)
                        }
                        val messageThreadsList = messageThreadsMap.entries.map {
                            val id = it.key
                            val messages = it.value
                            MessageThread(id, messages)
                        }

                        _messagesThreadsUiState.value = UIState.Success(data = messageThreadsList)
                    }
                }
            }
        }
    }

    fun postMessage(threadId: Int, messageBody: String) {
        viewModelScope.launch {
            mainRepository.postMessage(threadId, messageBody).collect { postMessageResponse ->
                when (postMessageResponse) {
                    is NetworkResult.Error -> {
                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        getMessagesThreads(refreshMessages = true, threadId = threadId)
                    }
                }

            }
        }
    }

    fun getConversationFromThread(threadId: Int) {
        viewModelScope.launch {
            _conversationUiState.value = UIState.Loading()
            val conversation = messageThreadsMap[threadId]
            val uiState = conversation?.let { data ->
                UIState.Success(data)
            } ?: UIState.Error(message = "Error fetching conversation")

            _conversationUiState.value = uiState
        }
    }

    suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    private fun getMessageThreadMap(messages: List<MessageDto>): Map<Int, List<MessageDto>> {
        // Map<Thread_id, list<Message>>
        val groupedMessages = messages.groupBy { it.threadId } as MutableMap

        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, hh:mm a", Locale.ENGLISH)
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

        for ((threadId, messageGroup) in groupedMessages) {
            groupedMessages[threadId] = messageGroup.sortedWith(compareBy<MessageDto> {
                LocalDateTime.parse(it.dateTime, inputFormatter).toLocalDate()
            }.thenBy {
                LocalDateTime.parse(it.dateTime, inputFormatter).toLocalTime()
            }).map {
                it.copy(
                    dateTime = LocalDateTime.parse(it.dateTime, inputFormatter)
                        .format(outputFormatter)
                )
            }
        }
        return groupedMessages
    }
}