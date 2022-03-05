package com.chatappapi.api.controller.contacts;

import com.chatappapi.api.service.ContactService;
import com.chatappapi.api.controller.contacts.dto.ContactSaveDto;
import com.chatappapi.api.controller.contacts.dto.ContactDto;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.util.JwtUtil;
import com.chatcomponents.Contact;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/chat/contact")
public class ContactController {

    private final ContactService contactService;
    private final JwtUtil jwtUtil;

    public ContactController(ContactService contactService, JwtUtil jwtUtil) {
        this.contactService = contactService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(path = "active-contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContactDto> getAllContacts(HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));
        return contactService.getAllContacts(emailLoggedUser);
    }

    @GetMapping(path = "find-contact/{idContact}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ContactDto getContactById(@PathVariable(name = "idContact") Long idContact,
                                     HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));
        return contactService.getContact(emailLoggedUser, idContact);
    }

    @PostMapping
    public ContactDto saveContact(@RequestBody ContactSaveDto contactProjection,
                                  HttpServletRequest request) {
        Contact contact = DozerConverter.parseObject(contactProjection, Contact.class);
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));

        return contactService.saveContact(contact, emailLoggedUser);
    }
}
