package com.example.domain.implementation.util

import com.example.domain.logic.core.RepositoryMessanger
import com.example.domain.logic.util.UpdateTokenUseCase

class UpdateTokenUcImpl(val repositoryMessanger: RepositoryMessanger):UpdateTokenUseCase {
    override fun invoke(token: String) {
        repositoryMessanger.updateToken(token)
    }
}