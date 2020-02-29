package com.example.domain.logic.status

import com.example.domain.logic.User

interface RecieveStatusUseCase {
    operator fun invoke(status: Status,user: User)
}