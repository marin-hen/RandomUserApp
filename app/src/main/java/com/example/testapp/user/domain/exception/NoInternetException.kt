package com.example.testapp.user.domain.exception

import java.io.IOException

class NoInternetException(cause: Throwable, message: String? = cause.localizedMessage) :
    IOException(message, cause)