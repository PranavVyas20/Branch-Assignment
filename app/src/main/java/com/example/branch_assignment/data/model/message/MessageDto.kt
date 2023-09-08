package com.example.branch_assignment.data.model.message

data class MessageDto(
    val id: Int,
    val threadId: Int,
    val userId: String,
    val agentId: String?,
    val messageBody: String,
    val dateTime: String,
)