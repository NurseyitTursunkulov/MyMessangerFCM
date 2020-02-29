package com.example.domain.status

sealed class Status() {
    class Read():Status()
    class Delivered():Status()
    class Typing():Status()
}