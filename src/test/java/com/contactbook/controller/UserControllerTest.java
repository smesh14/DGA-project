package com.contactbook.controller;

import com.contactbook.entity.ContactRequest;
import com.contactbook.entity.ContactResponse;
import com.contactbook.entity.UserRequest;
import com.contactbook.entity.UserResponse;
import com.contactbook.repository.ContactRepository;
import com.contactbook.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

import static com.contactbook.entity.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;





@SpringBootTest
class UserControllerTest {


    @Autowired
    private UserController userController;


    @Autowired
    private  UserRepository userRepository;


    @Autowired
    private ContactRepository contactRepository;


    private static final UserRequest USER_REQUEST = UserRequest.builder()
            .phoneNumber("12345678")
            .email("as@gmail.com")
            .password("Aaaaaaaa")
            .role(USER)
            .name("sandro")
            .build();

    private static final ContactRequest CONTACT_REQUEST = ContactRequest.builder()
            .phoneNumber("21345678")
            .name("levani")
            .build();

    private static final ContactRequest UPDATE_REQUEST = ContactRequest.builder()
            .phoneNumber("21345678")
            .name("levani2")
            .build();

    private static final String USER_NAME = "sandro";
    private static final String CONTACT_NAME = "levani";
    private static final String UPDATED_CONTACT_NAME = "levani2";


    @AfterEach
    public void afterEach() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DirtiesContext
    void register() {
        UserResponse result = userController.register(USER_REQUEST).getBody();

        assert result != null;
        assertEquals(USER_NAME, result.getName());
    }

    @Test
    @DirtiesContext
    void addContact() {
        UserResponse result = userController.register(USER_REQUEST).getBody();
        ContactResponse contactResponse = userController.addContact(CONTACT_REQUEST,USER_NAME).getBody();

        assert result != null;
        assert contactResponse != null;
        assertEquals(CONTACT_NAME, Objects.requireNonNull(userController.loadContact(result.getName(), contactResponse.getName()).getBody()).getName());
    }

    @Test
    @DirtiesContext
    void updateContact() {
        UserResponse result = userController.register(USER_REQUEST).getBody();
        ContactResponse contactResponse = userController.addContact(CONTACT_REQUEST,USER_NAME).getBody();
        assert contactResponse != null;
        ContactResponse updatedContactResponse = userController.updateContact(UPDATE_REQUEST,USER_NAME,contactResponse.getName()).getBody();

        assert result != null;
        assert updatedContactResponse != null;
        assertEquals(UPDATED_CONTACT_NAME, Objects.requireNonNull(userController.loadContact(result.getName(), updatedContactResponse.getName()).getBody()).getName());
    }

}