package com.example.domain.implementation.core

import com.example.domain.logic.User
import com.example.domain.logic.core.GetAllUsersUseCase
import com.example.domain.logic.core.RepositoryMessanger

class GetAllusersUcImpl(val repository: RepositoryMessanger) : GetAllUsersUseCase {
    override fun invoke(): List<User> {
      return repository.getAllUsers()
    }
}