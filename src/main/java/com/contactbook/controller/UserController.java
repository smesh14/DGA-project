package com.contactbook.controller;


import com.contactbook.entity.*;
import com.contactbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {



    @Autowired
    private UserService userService;



    @PostMapping("/user/create")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }

    @PostMapping("/user/add/contact/{name}")
    public ResponseEntity<ContactResponse> addContact(@RequestBody @Valid ContactRequest contactRequest, @PathVariable String name) {
        return ResponseEntity.ok(userService.addContact(contactRequest,name));
    }

    @PutMapping("/user/update/contact/{userName}/{contactName}")
    public ResponseEntity<ContactResponse> updateContact(@RequestBody @Valid ContactRequest contactRequest,@PathVariable String userName,@PathVariable String contactName) {
        return ResponseEntity.ok(userService.updateContact(contactRequest,userName,contactName));
    }

    @GetMapping("/user/load/contact/{userName}/{contactName}")
    public ResponseEntity<ContactResponse> loadContact(@PathVariable String userName,@PathVariable String contactName) {
        return ResponseEntity.ok(userService.loadContact(userName,contactName));
    }

    @GetMapping("/user/load/contact/{userName}")
    public ResponseEntity<Page<ContactResponse>>loadContacts(@PathVariable String userName, Pageable pageRequest) {
        return ResponseEntity.ok(userService.loadContacts(userName,pageRequest));
    }
}
