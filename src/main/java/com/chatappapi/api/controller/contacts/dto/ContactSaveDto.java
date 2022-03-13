package com.chatappapi.api.controller.contacts.dto;

import com.chatappapi.api.controller.users.dto.UserSearchDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ContactSaveDto implements Serializable {

    private UserSearchDto userB;
}
