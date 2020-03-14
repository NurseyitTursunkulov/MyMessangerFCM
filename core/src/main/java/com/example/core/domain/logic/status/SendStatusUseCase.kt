package com.example.domain.logic.status

import com.example.domain.logic.User

interface SendStatusUseCase {
    operator fun invoke(status: Status,user: User)
}