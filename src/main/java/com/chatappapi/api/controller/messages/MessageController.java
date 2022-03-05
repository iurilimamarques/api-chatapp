package com.chatappapi.api.controller.messages;

import com.chatappapi.api.service.MessageService;
import com.chatappapi.api.controller.messages.dto.MessageDto;
import com.chatappapi.api.util.ActiveUserChangeListener;
import com.chatcomponents.Message;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Transactional(propagation = Propagation.SUPPORTS)
@RestController
@RequestMapping("/chat/message")
public class MessageController implements ActiveUserChangeListener {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Transactional
    @MessageMapping("/chat")
    public void send(@Payload Message chatMessage) {
        messageService.sendMessage(chatMessage);
    }

    @Override
    public void notifyActiveUserChange() {
        messageService.notifyActiveUserChange();
    }

    @GetMapping(value = "/load-all-messages/{idContact}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDto> loadAllMessages(@PathVariable(name = "idContact") Long idContact) {
        return messageService.loadAllMessages(idContact);
    }

}
