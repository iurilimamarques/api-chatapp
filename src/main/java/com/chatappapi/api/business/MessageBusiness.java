package com.chatappapi.api.business;

import com.chatappapi.api.controller.messages.projection.MessageProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.MessageRepository;
import com.chatappapi.api.util.ActiveUserManager;
import com.chatcomponents.Contact;
import com.chatcomponents.Message;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.chatcomponents.QContact.contact;

@Component
public class MessageBusiness {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

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
        MessageProjection result = DozerConverter.parseObject(message, MessageProjection.class);

        webSocket.convertAndSendToUser(chatMessage.getUserRecipient().getEmail(), "/queue/messages", result);
    }

    @Transactional
    private Contact updateContact(Contact c) {
        Contact contact = contactRepository.getById(c.getId());
        contact.setUpdatedIn(LocalDateTime.now());
        return contactRepository.save(contact);
    }

    public List<MessageProjection> loadAllMessages(Long idContact) {
        BooleanExpression where = contact.id.eq(idContact);
        Iterable<Message> messages = messageRepository.findAll(where);

        List<Message> listMessages = ImmutableList.copyOf(messages);

        return DozerConverter.parseListObjects(listMessages, MessageProjection.class);
    }

    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
