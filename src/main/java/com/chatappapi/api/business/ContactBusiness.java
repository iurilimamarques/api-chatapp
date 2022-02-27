package com.chatappapi.api.business;

import com.chatappapi.api.controller.contacts.projection.ContactProjection;
import com.chatappapi.api.controller.messages.projection.UserProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.ContactRepository;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.Contact;
import com.chatcomponents.QUser;
import com.chatcomponents.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.chatcomponents.QContact.contact;

@Component
public class ContactBusiness {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ContactProjection> getAllContacts(String email) {
        Optional<User> loggedUser = userRepository.findOne(QUser.user.email.eq(email));
        BooleanExpression where = contact.userA.email.eq(email).or(contact.userB.email.eq(email));
        Iterable<Contact> contacts = contactRepository.findAll(where, contact.createdIn.desc(), contact.updatedIn.desc());

        List<Contact> listContacts = StreamSupport.stream(contacts.spliterator(), false)
                .collect(Collectors.toList());

        List<ContactProjection> contactsProjections = new ArrayList<>();

        if (listContacts.size() > 0) {
            listContacts.stream().forEach(contact -> {
                UserProjection userProjection = null;
                if (contact.getUserA().getId() == loggedUser.get().getId()) {
                    userProjection = new UserProjection(contact.getUserB().getId(), contact.getUserB().getName(), contact.getUserB().getEmail());
                } else {
                    userProjection = new UserProjection(contact.getUserA().getId(), contact.getUserA().getName(), contact.getUserA().getEmail());
                }

                ContactProjection ContactSaveProjection = new ContactProjection(contact.getId(), userProjection, contact.getUpdatedIn(), contact.getCreatedIn());
                contactsProjections.add(ContactSaveProjection);
            });
        }
        return contactsProjections;
    }

    public ContactProjection saveContact(Contact contact, String emailLoggedUser) {
        BooleanExpression where = QUser.user.email.eq(emailLoggedUser);
        Optional<User> userSender = userRepository.findOne(where);
        contact.setUserA(userSender.get());

        Contact savedContact = contactRepository.save(contact);

        UserProjection userProjection = new UserProjection();
        if (savedContact.getUserB().getId() == userSender.get().getId()) {
            userProjection = DozerConverter.parseObject(savedContact.getUserA(), UserProjection.class);
        } else {
            userProjection = DozerConverter.parseObject(savedContact.getUserB(), UserProjection.class);
        }

        return new ContactProjection(savedContact.getId(), userProjection, savedContact.getUpdatedIn(), savedContact.getCreatedIn());
    }
}
