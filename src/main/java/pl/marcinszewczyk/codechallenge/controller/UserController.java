package pl.marcinszewczyk.codechallenge.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinszewczyk.codechallenge.user.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{userToFollow}/follow")
    public void follow(@RequestParam String activeUser, @PathVariable String userToFollow) {
        userService.follow(activeUser, userToFollow);
    }
}
