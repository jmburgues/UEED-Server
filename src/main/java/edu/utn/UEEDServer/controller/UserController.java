package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public PostResponse add(@RequestBody User user)
    {
        return userService.add(user);
    }

    @GetMapping
    public List<User>getAll()
    {
        return userService.getAll();
    }

    @GetMapping
    public User getByUsername(@PathVariable String username)
    {
        return userService.getByUsername(username);
    }
}
