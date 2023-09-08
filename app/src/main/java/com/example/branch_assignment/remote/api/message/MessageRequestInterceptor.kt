package com.example.branch_assignment.remote.api.message

import com.example.branch_assignment.utils.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MessageRequestInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = runBlocking {
            tokenManager.getToken()
        }
        request.addHeader("X-Branch-Auth-Token", token!!)
        return chain.proceed(request.build())
    }
}