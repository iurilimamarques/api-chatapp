package com.chatappapi.api.controller.users.projection;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserSearchProjection implements Serializable {

    private long id;

    private String name;

    private String email;

    private LocalDateTime lastInterection;

    private String remoteAddress;
}
