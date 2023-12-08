package com.contactbook.entity;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ContactRequest {
    private String name;

    @Size(min = 9, max = 9, message = "phone number must be exactly 11 digits")
    @Pattern(regexp = "\\d+", message = "phone number must consist of digits only")
    private String phoneNumber;
}
