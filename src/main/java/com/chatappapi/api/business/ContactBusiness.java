package com.chatappapi.api.business;

import com.chatappapi.api.controller.contacts.projection.ContactProjection;
import com.chatappapi.api.controller.messages.projection.UserProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.Contact;
import com.chatcomponents.QUser;
import com.chatcomponents.User;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.chatcomponents.QContact.contact;

@Component
public class ContactBusiness {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ContactProjection> getAllContacts(String emailLoggedUser) {
        User loggedUser = getLoggedUser(emailLoggedUser);
        BooleanExpression where = contact.userA.email.eq(emailLoggedUser).or(contact.userB.email.eq(emailLoggedUser));
        Iterable<Contact> contacts = contactRepository.findAll(where, contact.updatedIn.desc(), contact.createdIn.desc());

        List<Contact> listContacts = ImmutableList.copyOf(contacts);;

        List<ContactProjection> contactsProjections = new ArrayList<>();

        if (listContacts.size() > 0) {
            listContacts.stream().forEach(contact -> {
                UserProjection userProjection = getUserRecipientOfContact(loggedUser, contact);

                ContactProjection ContactSaveProjection = new ContactProjection(contact.getId(), userProjection, contact.getUpdatedIn(), contact.getCreatedIn());
                contactsProjections.add(ContactSaveProjection);
            });
        }
        return contactsProjections;
    }

    private User getLoggedUser(String emailLoggedUser) {
        Optional<User> loggedUser = userRepository.findOne(QUser.user.email.eq(emailLoggedUser));
        return loggedUser.get();
    }

    private static UserProjection getUserRecipientOfContact(User loggedUser, Contact contact) {
        if (contact.getUserA().getId() == loggedUser.getId()) {
            return new UserProjection(contact.getUserB().getId(), contact.getUserB().getName(), contact.getUserB().getEmail());
        } else {
            return new UserProjection(contact.getUserA().getId(), contact.getUserA().getName(), contact.getUserA().getEmail());
        }
    }

    public ContactProjection getContact(String emailLoggedUser, Long idContact) {
        User loggedUser = getLoggedUser(emailLoggedUser);
        Optional<Contact> contact = contactRepository.findById(idContact);
        UserProjection userProjection = getUserRecipientOfContact(loggedUser, contact.get());
        return new ContactProjection(contact.get().getId(), userProjection, contact.get().getUpdatedIn(), contact.get().getCreatedIn());
    }

    public ContactProjection saveContact(Contact contact, String emailLoggedUser) {
        User loggedUser = getLoggedUser(emailLoggedUser);

        contact.setUserA(loggedUser);

        Contact savedContact = contactRepository.save(contact);

        UserProjection userProjection;
        if (savedContact.getUserB().getId() == loggedUser.getId()) {
            userProjection = DozerConverter.parseObject(savedContact.getUserA(), UserProjection.class);
        } else {
            userProjection = DozerConverter.parseObject(savedContact.getUserB(), UserProjection.class);
        }

        return new ContactProjection(savedContact.getId(), userProjection, savedContact.getUpdatedIn(), savedContact.getCreatedIn());
    }
}
