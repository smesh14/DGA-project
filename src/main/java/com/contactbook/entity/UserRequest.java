package com.contactbook.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequest {
    @Size(min = 9, max = 9, message = "phone number must be exactly 11 digits")
    @Pattern(regexp = "\\d+", message = "phone number must consist of digits only")
    private String phoneNumber;

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;


    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    private UserRole role;
}
