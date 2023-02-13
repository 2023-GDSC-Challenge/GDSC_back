package com.solution.green.controller;

import com.solution.green.entity.User;
import com.solution.green.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("get-all")
    public User getAll() throws Exception{
        return userService.getEmployee();
    }

    @PostMapping("save")
    public String save(@RequestBody User user) throws Exception {
        return userService.save(user);
    }
}