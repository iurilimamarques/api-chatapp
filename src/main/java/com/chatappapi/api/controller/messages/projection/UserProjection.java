package com.chatappapi.api.controller.messages.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProjection implements Serializable {

    private long id;

    private String name;

    private String email;

}
