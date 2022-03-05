package com.chatappapi.api.controller.users;

import com.chatappapi.api.business.UserBusiness;
import com.chatappapi.api.controller.users.projection.UserSearchProjection;
import com.chatappapi.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserBusiness userBusiness;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSearchProjection> findUser(@RequestParam(name = "keyWord") String keyWord,
                                               HttpServletRequest request) {
        String emailLoggedUser = jwtUtil.getUserNameFromJwtToken(request.getHeader("Authorization"));
        return userBusiness.findUser(keyWord, emailLoggedUser);
    }

}
