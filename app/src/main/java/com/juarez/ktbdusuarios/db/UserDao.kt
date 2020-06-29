package com.juarez.ktbdusuarios.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.juarez.ktbdusuarios.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User): Long

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE name IN (:query) OR firstSurname IN (:query) OR secondSurname IN (:query) OR ticketNumber IN (:query)")
    fun searchUsers(query: String): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE id IN (:id)")
    suspend fun getUser(id: Int): User

    @Query("SELECT * FROM users WHERE ticketNumber IN (:ticketNumber)")
    suspend fun getTicketNumber(ticketNumber: Int): User

    @Delete
    suspend fun deleteUser(article: User)
}