package com.contactbook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse {

    private String phoneNumber;
    private String email;
    private String name;
    private UserRole role;
}
