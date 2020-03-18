package com.example.core.domain.logic.core

interface LifeCycleObserver {
    fun observeNewMessages()
    fun unsubscribeFromNewMessages()
}