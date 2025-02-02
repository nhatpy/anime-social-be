package com.anime_social.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "EMAIL_IS_INVALID")
    String email;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    @Size(min = 6, message = "PASSWORD_AT_LEAST_6_CHARACTERS")
    String password;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String fullName;
}
