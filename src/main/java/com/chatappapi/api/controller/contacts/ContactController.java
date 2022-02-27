package com.chatappapi.api.controller.contacts;

import com.chatappapi.api.business.ContactBusiness;
import com.chatappapi.api.controller.contacts.projection.ContactSaveProjection;
import com.chatappapi.api.controller.contacts.projection.ContactProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.util.JwtUtil;
import com.chatcomponents.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/chat/contact")
public class ContactController {

    @Autowired
    private ContactBusiness contactBusiness;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(path = "active-contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContactProjection> getAllContacts(HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));
        return contactBusiness.getAllContacts(emailLoggedUser);
    }

    @PostMapping
    public ContactProjection saveContact(@RequestBody ContactSaveProjection contactProjection,
                                         HttpServletRequest request) {
        Contact contact = DozerConverter.parseObject(contactProjection, Contact.class);
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));

        return contactBusiness.saveContact(contact, emailLoggedUser);
    }
}
