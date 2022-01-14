package com.chatappapi.api.business;

import com.chatappapi.api.controller.projection.ContactProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.Contact;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.chatcomponents.QContact.contact;

@Component
public class ContactBusiness {

    @Autowired
    private ContactRepository contactRepository;

    public List<ContactProjection> getAllContacts(Long loggedUser) {
        BooleanExpression where = contact.userA.id.eq(loggedUser).or(contact.userB.id.eq(loggedUser));
        Iterable<Contact> contacts = contactRepository.findAll(where, contact.createdIn.desc(), contact.updatedIn.desc());

        List<Contact> listContacts = StreamSupport.stream(contacts.spliterator(), false)
                .collect(Collectors.toList());

        return DozerConverter.parseListObjects(listContacts, ContactProjection.class);
    }
}
