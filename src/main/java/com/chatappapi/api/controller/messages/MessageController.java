package com.chatappapi.api.controller.messages;

import com.chatappapi.api.business.MessageBusiness;
import com.chatappapi.api.controller.messages.projection.MessageProjection;
import com.chatappapi.api.util.ActiveUserChangeListener;
import com.chatcomponents.Message;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageBusiness messageBusiness;

    @Transactional
    @MessageMapping("/chat")
    public void send(@Payload Message chatMessage) {
        messageBusiness.sendMessage(chatMessage);
    }

    @Override
    public void notifyActiveUserChange() {
        messageBusiness.notifyActiveUserChange();
    }

    @GetMapping(value = "/load-all-messages/{idContact}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageProjection> loadAllMessages(@PathVariable(name = "idContact") Long idContact) {
        return messageBusiness.loadAllMessages(idContact);
    }

}
