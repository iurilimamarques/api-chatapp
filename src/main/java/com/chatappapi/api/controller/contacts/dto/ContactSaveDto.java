package com.chatappapi.api.controller.contacts.dto;

import com.chatappapi.api.controller.users.dto.UserSearchDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContactSaveDto implements Serializable {

    private long id;

    private UserSearchDto userA;

    private UserSearchDto userB;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
