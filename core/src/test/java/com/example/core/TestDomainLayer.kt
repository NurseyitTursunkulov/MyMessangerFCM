package com.example.core

import com.example.core.comunicator.Message
import com.example.core.domain.implementation.MessangerDomainImpl
import com.example.core.domain.logic.core.Chat
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class TestDomainLayer {
    val fakeRepositoryMessanger = FakeRepositoryMessangerIml()
    var messangerDomain = MessangerDomainImpl(fakeRepositoryMessanger)

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()


    @Test
    fun `sent message is recieved in flow`() = runBlockingTest {
        fakeRepositoryMessanger.channel = channelBuilder<Message>(Channel.UNLIMITED){}
        val message = Message(text="hello world")
        messangerDomain.sendMessage(Chat(), message)
        messangerDomain.getChatMessagesFlow("").test(this) {
            assertValue(message)
        }
    }

    @Test
    fun `if message is already in chat flow should not emmit`() = runBlockingTest {
        fakeRepositoryMessanger.channel = channelBuilder<Message>(Channel.UNLIMITED){}
        val message = Message(text="hello world")
        messangerDomain.currentChat = Chat(messages = mutableListOf<Message>(message))
        messangerDomain.sendMessage(Chat(), message)
        messangerDomain.getChatMessagesFlow("").test(this) {
            assertNoValues()
        }
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}