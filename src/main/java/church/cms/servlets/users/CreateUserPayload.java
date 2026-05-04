package church.cms.servlets.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;

public record CreateUserPayload(
        @NotNull
        @Size(min = 3, max = 100, message = "Invalid user first name. The user's first name must have between 3 and 100 characters.")
        String firstName,

        @NotNull
        @Size(min = 3, max = 100, message = "Invalid user last name. The user's last name must have between 3 and 100 characters.")
        String lastName,

        @NotNull
        @Email(message = "Invalid user email address.")
        String email,

        @NotNull
        @Size(min = 8, max = 255, message = "Invalid user password. The user's password mush have between 8 and 255 characters.")
        String password,

        @NotNull
        @BooleanFlag
        Boolean isAdmin
) {
}