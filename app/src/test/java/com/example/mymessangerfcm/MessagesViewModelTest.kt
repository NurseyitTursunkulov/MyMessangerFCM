package com.example.mymessangerfcm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.comunicator.Message
import com.example.core.domain.logic.core.Chat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.channels.Channel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MessagesViewModelTest {
    // Set the main coroutines dispa
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var messangerViewModel: MessangerViewModel
    lateinit var fakeDomain: FakeDomain
    lateinit var messageObject: Message
    val messageText = "Message()"
    val chat = Chat(userIds = mutableListOf(""))

    @Before
    fun setupViewModel() {
        fakeDomain = FakeDomain()
        messangerViewModel = MessangerViewModel(fakeDomain)
        messageObject = Message(
            text = messageText,
            recipientId = chat.userIds.first(),
            senderId = fakeDomain.currentUser?.id ?: chat.userIds[1],
            senderName = fakeDomain.currentUser?.name ?: ""
        )
    }

    @InternalCoroutinesApi
    @Test
    fun `sent message is recieved in liveData`() = runBlockingTest {
        fakeDomain.channel = channelBuilder<Message>(Channel.UNLIMITED) {}

        messangerViewModel.openChat(chat)
        messangerViewModel.sendMessage(chat, messageObject)
        messangerViewModel.observeChatForNewMessages("")

        assertThat((LiveDataTestUtil.getValue(messangerViewModel.newMessageLiveData) as Message).text).isEqualTo(
            messageText
        )
    }

    @InternalCoroutinesApi
    @Test
    fun `sent message is added to chatList`() = runBlockingTest {
        fakeDomain.channel = channelBuilder<Message>(Channel.UNLIMITED) {}

        messangerViewModel.openChat(chat)
        messangerViewModel.sendMessage(chat, messageObject)
        messangerViewModel.observeChatForNewMessages("")

        assertThat(
            ((messangerViewModel.navigateToChatEvent.value?.peekContent() as Chat).messages).contains(
                messageObject
            )
        )
    }

    @InternalCoroutinesApi
    @Test
    fun `incoming message from companion trigers playSound event`() = runBlockingTest {
        fakeDomain.channel = channelBuilder<Message>(Channel.UNLIMITED) {}
        val messageObject = Message(
            text = messageText,
            recipientId = chat.userIds.first(),
            senderId = "otherUserId",
            senderName = fakeDomain.currentUser?.name ?: ""
        )

        messangerViewModel.openChat(chat)
        messangerViewModel.sendMessage(chat, messageObject)
        messangerViewModel.observeChatForNewMessages("")

        assertThat(LiveDataTestUtil.getValue(messangerViewModel.playSoundEvent)).isNotNull()
    }

    @InternalCoroutinesApi
    @Test
    fun `sent message from sender does not trigers playSound event`() = runBlockingTest {
        fakeDomain.channel = channelBuilder<Message>(Channel.UNLIMITED) {}

        messangerViewModel.openChat(chat)
        messangerViewModel.sendMessage(chat, messageObject)
        messangerViewModel.observeChatForNewMessages("")

        assertThat(LiveDataTestUtil.getValue(messangerViewModel.playSoundEvent)).isNull()
    }
}