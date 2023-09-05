package com.example.branch_assignment.remote.api.message

import com.example.branch_assignment.data.model.message.Message
import com.example.branch_assignment.data.model.message.PostMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface MessageApi {

    @GET("api/messages")
    suspend fun getAllMessagesThread(
    ): Response<List<Message>>

    @POST("api/messages")
    suspend fun postMessage(
        @Body request: PostMessageRequest
    ): Response<Message>
}