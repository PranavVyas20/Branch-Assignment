package com.example.branch_assignment.data.model.message

import com.google.gson.annotations.SerializedName

data class PostMessageRequest(
    @SerializedName("thread_id")
    val threadId: Int,
    @SerializedName("body")
    val messageBody: String
)
