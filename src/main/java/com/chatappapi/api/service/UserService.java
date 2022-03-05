package com.chatappapi.api.service;

import com.chatappapi.api.controller.users.dto.UserSearchDto;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.User;
import com.chatcomponents.UserStatus;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.chatcomponents.QUser.user;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserSearchDto> findUser(String keyWord, String emailLoggedUser, String idsSelected) {
        List<Long> notInIds = new ArrayList<>();
        if (!idsSelected.isEmpty()) {
            notInIds = Arrays.stream(idsSelected.split(",")).map(Long::valueOf).collect(Collectors.toList());
        }

        BooleanExpression where = user.email.contains(keyWord)
                .or(user.name.contains(keyWord))
                .and(user.email.ne(emailLoggedUser))
                .and(user.status.eq(UserStatus.ENABLED))
                .and(user.id.notIn(notInIds));

        Iterable<User> usersIterator = userRepository.findAll(where);
        List<User> userList = ImmutableList.copyOf(usersIterator);
        return DozerConverter.parseListObjects(userList, UserSearchDto.class);
    }
}
