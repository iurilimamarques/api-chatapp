package com.chatappapi.api.controller.users.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserSearchDto implements Serializable {

    private long id;

    private String name;

    private String email;

    private LocalDateTime lastInterection;

    private String remoteAddress;
}
