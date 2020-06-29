package com.juarez.ktbdusuarios.repository

import com.juarez.ktbdusuarios.db.UserDatabase
import com.juarez.ktbdusuarios.models.User

class UserRepository(private val db: UserDatabase) {

    suspend fun upsert(user: User) = db.getUserDao().upsert(user)

    fun getUsers() = db.getUserDao().getAllUsers()

    fun searchUsers(query: String) = db.getUserDao().searchUsers(query)

    suspend fun getTicketNumber(ticketNumber: Int) = db.getUserDao().getTicketNumber(ticketNumber)

    suspend fun getUser(userId: Int): User = db.getUserDao().getUser(userId)

    suspend fun deleteUser(user: User) = db.getUserDao().deleteUser(user)
}