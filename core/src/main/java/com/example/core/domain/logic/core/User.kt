package com.example.core.domain.logic.core

import java.util.*

data class User(
    var id:String= UUID.randomUUID().toString(),
    var name: String = "",
    var bio: String="",
    var profilePicturePath: String?=null
)