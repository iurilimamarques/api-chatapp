package com.chatappapi.api.controller.messages;

import com.chatappapi.api.controller.messages.projection.MessageProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.MessageRepository;
import com.chatappapi.api.util.ActiveUserChangeListener;
import com.chatappapi.api.util.ActiveUserManager;
import com.chatcomponents.Message;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.chatcomponents.QContact.contact;

@RestController
@RequestMapping("/chat/message")
public class MessageController implements ActiveUserChangeListener {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload Message chatMessage) throws Exception {
        Message message = Message.builder()
                .message(chatMessage.getMessage())
                .contact(chatMessage.getContact())
                .userSender(chatMessage.getUserSender())
                .userRecipient(chatMessage.getUserRecipient())
                .build();
        messageRepository.save(message);
        MessageProjection result = DozerConverter.parseObject(message, MessageProjection.class);

        webSocket.convertAndSendToUser(chatMessage.getUserRecipient().getEmail(), "/queue/messages", result);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }

    @GetMapping(value = "/load-all-messages/{idContact}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageProjection> loadAllMessages(@PathVariable(name = "idContact") Long idContact,
                                           HttpServletRequest request) {
        BooleanExpression where = contact.id.eq(idContact);
        Iterable<Message> messages = messageRepository.findAll(where);

        List<Message> listMessages = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());

        return DozerConverter.parseListObjects(listMessages, MessageProjection.class);
    }

}
