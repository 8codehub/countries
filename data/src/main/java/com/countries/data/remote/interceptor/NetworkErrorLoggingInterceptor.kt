package com.countries.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

internal class NetworkErrorLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            Log.e("NETWORK_ERROR", "Request failed: ${e.message}", e)
            throw e
        }
    }
}