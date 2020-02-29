package com.example.domain.core

import com.example.domain.User

interface GetAllUsersUseCase {
    operator fun invoke():List<User>
}