package com.example.testapp.user.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapp.user.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}