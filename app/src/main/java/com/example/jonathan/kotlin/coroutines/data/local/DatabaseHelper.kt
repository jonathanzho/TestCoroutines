package com.example.jonathan.kotlin.coroutines.data.local

import com.example.jonathan.kotlin.coroutines.data.local.entity.User

interface DatabaseHelper {

    suspend fun getUsers(): List<User>

    suspend fun insertAll(users: List<User>)

}