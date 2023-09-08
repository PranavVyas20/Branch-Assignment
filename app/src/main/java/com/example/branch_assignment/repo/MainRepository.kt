package com.example.branch_assignment.repo

import com.example.branch_assignment.data.model.login.LoginRequest
import com.example.branch_assignment.data.model.login.LoginResponse
import com.example.branch_assignment.data.model.message.MessageDto
import com.example.branch_assignment.data.model.message.MessageResponse
import com.example.branch_assignment.data.model.message.PostMessageRequest
import com.example.branch_assignment.data.model.message.toMessageDto
import com.example.branch_assignment.remote.api.auth.AuthApi
import com.example.branch_assignment.remote.api.message.MessageApi
import com.example.branch_assignment.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val authApi: AuthApi,
    private val messageAPpi: MessageApi
) {
    suspend fun loginUser(username: String, password: String): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val loginResponse = authApi.loginUser(request = LoginRequest(username, password))
                if (loginResponse.isSuccessful) {
                    val body = loginResponse.body()
                    emit(NetworkResult.Success(body!!))
                } else {
                    emit(NetworkResult.Error(message = loginResponse.message()))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    suspend fun getAllMessages(): Flow<NetworkResult<List<MessageDto>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val messagesResponse = messageAPpi.getAllMessagesThread()
                if (messagesResponse.isSuccessful) {
                    val body = messagesResponse.body()
                    emit(NetworkResult.Success(body!!.map { it.toMessageDto() }))
                } else {
                    emit(NetworkResult.Error(message = messagesResponse.message()))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    suspend fun postMessage(threadId: Int, messageBody: String): Flow<NetworkResult<MessageResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val postMessagesResponse =
                    messageAPpi.postMessage(request = PostMessageRequest(threadId, messageBody))
                if (postMessagesResponse.isSuccessful) {
                    val body = postMessagesResponse.body()
                    emit(NetworkResult.Success(body!!))
                } else {
                    emit(NetworkResult.Error(message = postMessagesResponse.message()))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }
}