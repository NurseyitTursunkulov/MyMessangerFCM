package com.example.domain.implementation

import com.example.domain.logic.User

class UserImpl(override var name: String, override var token: String) :User {
}