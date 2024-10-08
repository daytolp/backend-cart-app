package com.daytolp.cartapp.backend_cart_app.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    @Size(min = 4, max = 10)
    @NotBlank
    private String username;

    @Email
    @NotEmpty
    private String email;
}
