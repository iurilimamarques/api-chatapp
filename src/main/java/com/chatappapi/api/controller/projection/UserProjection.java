package com.chatappapi.api.controller.projection;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserProjection implements Serializable {

    private long id;

    private String name;

    private String email;
}
