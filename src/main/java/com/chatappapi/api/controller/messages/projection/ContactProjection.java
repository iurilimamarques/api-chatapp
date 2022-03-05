package com.chatappapi.api.controller.messages.projection;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContactProjection implements Serializable {

    private long id;

    private UserProjection user;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
