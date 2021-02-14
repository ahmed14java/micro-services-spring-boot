package com.photoappuser.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupPayload {

    @NotBlank(message = "Name can not be empty")
    @Size(min = 2, message = "Name must not be less than two characters")
    private String name;

    @NotBlank(message = "Username can not be empty")
    @Size(min = 2, message = "Username must not be less than two characters")
    private String username;

    @NotBlank(message = "Email can not be empty")
    @Email
    private String email;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 8, max = 16 , message = "Password must be equal or greater than 8 characters and less than 16 characters")
    private String password;
}
