package com.example.domain.status

import com.example.domain.User

interface SendStatusUseCase {
    operator fun invoke(status: Status,user: User)
}