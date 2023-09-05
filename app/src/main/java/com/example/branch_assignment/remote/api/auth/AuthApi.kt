package com.example.branch_assignment.remote.api.auth

import com.example.branch_assignment.data.model.login.LoginRequest
import com.example.branch_assignment.data.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers("Content-Type:application/json")
    @POST("api/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}