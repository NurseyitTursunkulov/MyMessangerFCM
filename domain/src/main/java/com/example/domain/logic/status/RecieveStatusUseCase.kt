package com.example.domain.logic.status

import androidx.lifecycle.LiveData
import com.example.domain.logic.User

interface RecieveStatusUseCase {
    operator fun invoke():LiveData<Status>
}