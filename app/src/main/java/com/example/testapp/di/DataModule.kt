package com.example.testapp.di

import com.example.testapp.user.data.remote.UsersApi
import com.example.testapp.user.data.UsersRepositoryImpl
import com.example.testapp.user.data.local.UsersDao
import com.example.testapp.user.domain.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit) : UsersApi {
        return retrofit.create(UsersApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUsersRepository(api: UsersApi, usersDao: UsersDao) : UsersRepository {
        return UsersRepositoryImpl(api, usersDao)
    }
}
