package com.juarez.ktbdusuarios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.ktbdusuarios.models.User
import com.juarez.ktbdusuarios.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun saveUser(user: User) = viewModelScope.launch {
        userRepository.upsert(user)
    }

    fun getSavedUsers() = userRepository.getUsers()

    fun searchUsers(query: String) = userRepository.searchUsers(query)

    suspend fun getUser(userId: Int): User = userRepository.getUser(userId)

    suspend fun getTicketNumber(ticketNumber: Int): User = userRepository.getTicketNumber(ticketNumber)

    fun deleteUser(user: User) = viewModelScope.launch {
        userRepository.deleteUser(user)
    }
}