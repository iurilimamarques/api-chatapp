package com.chatappapi.api.service;

import com.chatappapi.api.controller.contacts.dto.ContactDto;
import com.chatappapi.api.controller.messages.dto.UserDto;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.Contact;
import com.chatcomponents.QContact;
import com.chatcomponents.QUser;
import com.chatcomponents.User;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.chatcomponents.QContact.contact;

@Component
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public List<ContactDto> getAllContacts(String emailLoggedUser) {
        User loggedUser = getLoggedUser(emailLoggedUser);
        BooleanExpression where = contact.userA.email.eq(emailLoggedUser).or(contact.userB.email.eq(emailLoggedUser));
        Iterable<Contact> contacts = contactRepository.findAll(where, contact.updatedIn.desc(), contact.createdIn.desc());

        List<Contact> listContacts = ImmutableList.copyOf(contacts);;

        List<ContactDto> contactsProjections = new ArrayList<>();

        if (listContacts.size() > 0) {
            listContacts.stream().forEach(contact -> {
                UserDto userDto = getUserRecipientOfContact(loggedUser, contact);

                ContactDto ContactSaveProjection = new ContactDto(contact.getId(), userDto, contact.getUpdatedIn(), contact.getCreatedIn());
                contactsProjections.add(ContactSaveProjection);
            });
        }
        return contactsProjections;
    }

    private User getLoggedUser(String emailLoggedUser) {
        Optional<User> loggedUser = userRepository.findOne(QUser.user.email.eq(emailLoggedUser));
        return loggedUser.get();
    }

    private static UserDto getUserRecipientOfContact(User loggedUser, Contact contact) {
        if (contact.getUserA().getId() == loggedUser.getId()) {
            return new UserDto(contact.getUserB().getId(), contact.getUserB().getName(), contact.getUserB().getEmail());
        } else {
            return new UserDto(contact.getUserA().getId(), contact.getUserA().getName(), contact.getUserA().getEmail());
        }
    }

    public ContactDto getContact(String emailLoggedUser, Long idContact) {
        User loggedUser = getLoggedUser(emailLoggedUser);
        Optional<Contact> contact = contactRepository.findById(idContact);
        UserDto userDto = getUserRecipientOfContact(loggedUser, contact.get());
        return new ContactDto(contact.get().getId(), userDto, contact.get().getUpdatedIn(), contact.get().getCreatedIn());
    }

    private Optional<Contact> verifyContactExistence(User loggedUser, UserDto userRecipient) {
        BooleanExpression where = contact.userA.id.eq(loggedUser.getId()).or(contact.userB.id.eq(loggedUser.getId()))
                .and(QContact.contact.userA.id.eq(userRecipient.getId()).or(QContact.contact.userB.id.eq(userRecipient.getId())));
        return contactRepository.findOne(where);
    }

    public ContactDto saveContact(Contact contact, String emailLoggedUser) {
        User loggedUser = getLoggedUser(emailLoggedUser);

        UserDto userRecipient = new UserDto(contact.getUserB().getId(), contact.getUserB().getName(), contact.getUserB().getEmail());

        Optional<Contact> contactExistent = verifyContactExistence(loggedUser, userRecipient);

        if (contactExistent.isPresent()) {
            return new ContactDto(contactExistent.get().getId(), userRecipient, contactExistent.get().getUpdatedIn(), contactExistent.get().getCreatedIn());
        } else {
            contact.setUserA(loggedUser);
            Contact savedContact = contactRepository.save(contact);
            return new ContactDto(savedContact.getId(), userRecipient, savedContact.getUpdatedIn(), savedContact.getCreatedIn());
        }
    }
}
