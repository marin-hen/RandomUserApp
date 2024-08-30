package com.example.testapp.common.network.interceptor

import com.example.testapp.user.domain.exception.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.UnknownHostException
import kotlin.reflect.KClass

class ErrorInterceptor : Interceptor {

    @Suppress("TooGenericExceptionCaught")
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            throw getMappedException(e)
        }
    }

    private fun getMappedException(error: Throwable): Throwable {
        val key = errorMapping.keys.find { it.isInstance(error) }
        return key?.let { errorMapping[it]?.invoke(error) ?: error } ?: error
    }

    companion object {
        private val errorMapping: Map<KClass<out Throwable>, (Throwable) -> IOException> = mapOf(
            UnknownHostException::class to { cause -> NoInternetException(cause) }
        )
    }
}