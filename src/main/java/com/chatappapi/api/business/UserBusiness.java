package com.chatappapi.api.business;

import com.chatappapi.api.controller.users.projection.UserSearchProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.User;
import com.chatcomponents.UserStatus;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.chatcomponents.QUser.user;

@Component
public class UserBusiness {

    @Autowired
    private UserRepository userRepository;

    public List<UserSearchProjection> findUser(String keyWord, String emailLoggedUser) {
        Iterable<User> usersIterator = userRepository.findAll(user.email.contains(keyWord).or(user.name.contains(keyWord)).and(user.email.ne(emailLoggedUser)).and(user.status.eq(UserStatus.ENABLED)));
        List<User> userList = ImmutableList.copyOf(usersIterator);
        return DozerConverter.parseListObjects(userList, UserSearchProjection.class);
    }
}
