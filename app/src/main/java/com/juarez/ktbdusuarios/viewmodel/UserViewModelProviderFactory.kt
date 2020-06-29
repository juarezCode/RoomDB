package com.juarez.ktbdusuarios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juarez.ktbdusuarios.repository.UserRepository

class UserViewModelProviderFactory(
    private val userRepository: UserRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}