package com.example.domain.status

import com.example.domain.User

interface RecieveStatusUseCase {
    operator fun invoke(status: Status,user: User)
}