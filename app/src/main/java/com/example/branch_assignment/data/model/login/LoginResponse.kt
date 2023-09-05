package com.example.branch_assignment.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("auth_token")
    val authToken: String
)
