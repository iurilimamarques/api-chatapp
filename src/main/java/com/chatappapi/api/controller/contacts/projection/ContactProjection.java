package com.chatappapi.api.controller.contacts.projection;

import com.chatappapi.api.controller.messages.projection.UserProjection;
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
public class ContactProjection implements Serializable {

    private long id;

    private UserProjection user;

    private LocalDateTime updatedIn;

    private LocalDateTime createdIn;
}
