package com.example.common.network.exception

import java.io.IOException

class NoInternetException(cause: Throwable, message: String? = cause.localizedMessage) :
    IOException(message, cause)