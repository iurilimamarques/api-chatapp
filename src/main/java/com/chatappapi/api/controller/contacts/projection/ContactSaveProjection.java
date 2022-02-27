package com.chatappapi.api.controller.contacts.projection;

import com.chatappapi.api.controller.users.projection.UserSearchProjection;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContactSaveProjection implements Serializable {

    private long id;

    private UserSearchProjection userA;

    private UserSearchProjection userB;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
