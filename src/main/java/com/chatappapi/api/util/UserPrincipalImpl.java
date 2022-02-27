package com.chatappapi.api.util;

import java.io.Serializable;
import java.security.Principal;

public class UserPrincipalImpl implements Serializable, Principal {

    private String email;

    UserPrincipalImpl(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
