package com.example.branch_assignment.data.model.message

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("thread_id")
    val threadId: Int?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("agent_id")
    val agentId: String?,
    @SerializedName("body")
    val messageBody: String?,
    @SerializedName("timestamp")
    val timestamp: String?
)
