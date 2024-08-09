package com.example.testapp.user.data.remote

import com.example.testapp.user.data.remote.model.UsersDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {

    @GET("api/")
    suspend fun getUsers(
        @Query("results") results: Int = 100,
    ): UsersDto
}