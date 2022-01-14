package com.chatappapi.api.controller;

import com.chatappapi.api.repository.UserRepository;
import com.chatcomponents.QUser;
import com.chatcomponents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findUser(@RequestParam(name = "keyWord") String keyWord,
                               @RequestParam(name = "loggedUser") Long loggedUser) {
        Iterable<User> usersIterator = userRepository.findAll(QUser.user.email.contains(keyWord).or(QUser.user.name.contains(keyWord)).and(QUser.user.id.ne(loggedUser)));

        return StreamSupport.stream(usersIterator.spliterator(), false)
                .collect(Collectors.toList());
    }

}
