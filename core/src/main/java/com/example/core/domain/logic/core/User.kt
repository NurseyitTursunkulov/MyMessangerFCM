package com.example.core.domain.logic.core

data class User(val name: String,
                val bio: String,
                val profilePicturePath: String?) {
    constructor(): this("", "", null)
}