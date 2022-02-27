package com.chatappapi.api.controller.users;

import com.chatappapi.api.controller.users.projection.UserSearchProjection;
import com.chatappapi.api.converter.DozerConverter;
import com.chatappapi.api.repository.UserRepository;
import com.chatappapi.api.util.JwtUtil;
import static com.chatcomponents.QUser.user;
import com.chatcomponents.User;
import com.chatcomponents.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/chat/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSearchProjection> findUser(@RequestParam(name = "keyWord") String keyWord,
                                               HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));

        Iterable<User> usersIterator = userRepository.findAll(user.email.contains(keyWord).or(user.name.contains(keyWord)).and(user.email.ne(emailLoggedUser)).and(user.status.eq(UserStatus.ENABLED)));

        return DozerConverter.parseListObjects(StreamSupport.stream(usersIterator.spliterator(), false)
                .collect(Collectors.toList()), UserSearchProjection.class);
    }

}
