package com.chatappapi.api.controller.messages.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContactDto implements Serializable {

    private long id;

    private UserDto user;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
