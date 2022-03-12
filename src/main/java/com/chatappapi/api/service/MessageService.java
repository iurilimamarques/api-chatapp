package com.chatappapi.api.service;

import com.chatappapi.api.controller.messages.dto.MessageDto;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.MessageRepository;
import com.chatappapi.api.util.ActiveUserManager;
import com.chatcomponents.Contact;
import com.chatcomponents.Message;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.chatcomponents.QContact.contact;

@Component
public class MessageService {

    private final ContactRepository contactRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate webSocket;
    private final ActiveUserManager activeUserManager;

    public MessageService(ContactRepository contactRepository, MessageRepository messageRepository, SimpMessagingTemplate webSocket, ActiveUserManager activeUserManager) {
        this.contactRepository = contactRepository;
        this.messageRepository = messageRepository;
        this.webSocket = webSocket;
        this.activeUserManager = activeUserManager;
    }

    @Transactional
    public void sendMessage(Message chatMessage) {
        Message message = Message.builder()
                .message(chatMessage.getMessage())
                .contact(chatMessage.getContact())
                .userSender(chatMessage.getUserSender())
                .userRecipient(chatMessage.getUserRecipient())
                .build();
        messageRepository.save(message);
        message.setContact(updateContact(message.getContact()));
        MessageDto result = DozerConverter.parseObject(message, MessageDto.class);

        webSocket.convertAndSendToUser(chatMessage.getUserRecipient().getEmail(), "/queue/messages", result);
    }

    @Transactional
    private Contact updateContact(Contact c) {
        Optional<Contact> contact = contactRepository.findById(c.getId());
        contact.get().setUpdatedIn(LocalDateTime.now());
        return contactRepository.save(contact.get());
    }

    public List<MessageDto> loadAllMessages(Long idContact) {
        BooleanExpression where = contact.id.eq(idContact);
        Iterable<Message> messages = messageRepository.findAll(where);

        List<Message> listMessages = ImmutableList.copyOf(messages);

        return DozerConverter.parseListObjects(listMessages, MessageDto.class);
    }

    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
