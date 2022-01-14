package com.chatappapi.api.controller;

import com.chatappapi.api.business.ContactBusiness;
import com.chatappapi.api.controller.projection.ContactProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat/contact")
public class ContactController {

    @Autowired
    private ContactBusiness contactBusiness;

    @GetMapping(path = "active-contacts/{loggedUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContactProjection> getAllContacts(@PathVariable("loggedUser") Long loggedUser) {
        return contactBusiness.getAllContacts(loggedUser);
    }
}
