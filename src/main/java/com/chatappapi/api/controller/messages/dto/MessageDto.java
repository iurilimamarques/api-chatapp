package com.chatappapi.api.controller.messages.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto implements Serializable {

    private Long id;

    private String message;

    @JsonFormat(pattern="HH:mm dd/MM/yyyy")
    private LocalDateTime createdIn;

    private UserDto userSender;

    private UserDto userRecipient;

    private ContactDto contact;

}
