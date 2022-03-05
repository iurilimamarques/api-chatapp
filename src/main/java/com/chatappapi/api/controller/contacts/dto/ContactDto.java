package com.chatappapi.api.controller.contacts.dto;

import com.chatappapi.api.controller.messages.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto implements Serializable {

    private long id;

    private UserDto user;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
