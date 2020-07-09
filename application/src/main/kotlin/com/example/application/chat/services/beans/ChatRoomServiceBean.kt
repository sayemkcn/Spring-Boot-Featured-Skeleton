package com.example.application.chat.services.beans

import com.example.application.chat.models.entities.ChatRoom
import com.example.application.chat.repositories.ChatRoomRepository
import com.example.application.chat.services.ChatRoomService
import com.example.coreweb.utils.PageAttr
import com.example.common.utils.ExceptionUtil
import com.example.application.domains.users.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomServiceBean @Autowired constructor(
        private val chatRoomRepository: ChatRoomRepository,
        private val userRepository: UserRepository
) : ChatRoomService {


    override fun search(query: String, username: String, page: Int, size: Int): Page<ChatRoom> {
        val user = this.userRepository.findByUsername(username).orElseThrow { ExceptionUtil.getNotFound("Chatroom", username) }
        return this.chatRoomRepository.search(query, user.id, PageAttr.getPageRequest(page, size))
    }

    override fun search(query: String, page: Int, size: Int): Page<ChatRoom> {
        return this.chatRoomRepository.search(query, PageAttr.getPageRequest(page))
    }

    override fun save(entity: ChatRoom): ChatRoom {
        return this.chatRoomRepository.save(entity)
    }

    override fun find(id: Long): Optional<ChatRoom> {
        return this.chatRoomRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val chatRoom = this.find(id).orElseThrow { ExceptionUtil.getNotFound("ChatRoom", id) }
            chatRoom.isDeleted = true
            this.save(chatRoom)
            return
        }
        return this.chatRoomRepository.deleteById(id)
    }

}