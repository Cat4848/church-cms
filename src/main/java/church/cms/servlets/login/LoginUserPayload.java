package church.cms.servlets.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginUserPayload(
        @NotNull
        @Email(message = "Invalid user email address")
        String email,

        @NotNull
        @Size(min = 8, max = 255, message = "Invalid user password. The user's password mush have between 8 and 255 characters.")
        String password
) {
}