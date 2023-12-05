package com.contactbook.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequest {
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private String name;
    private UserRole role;
}
