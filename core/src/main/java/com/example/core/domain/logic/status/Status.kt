package com.example.domain.logic.status

sealed class Status() {
    class Read():Status()
    class Delivered():Status()
    class Typing():Status()
}