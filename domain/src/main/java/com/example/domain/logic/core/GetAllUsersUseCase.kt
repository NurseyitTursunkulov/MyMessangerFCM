package com.example.domain.logic.core

import com.example.domain.logic.User

interface GetAllUsersUseCase {
    operator fun invoke():List<User>
}