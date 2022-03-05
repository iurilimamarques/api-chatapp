package com.chatappapi.api.controller.users;

import com.chatappapi.api.service.UserService;
import com.chatappapi.api.controller.users.dto.UserSearchDto;
import com.chatappapi.api.util.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/chat/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSearchDto> findUser(@RequestParam(name = "keyWord") String keyWord,
                                        HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));
        return userService.findUser(keyWord, emailLoggedUser);
    }

}
