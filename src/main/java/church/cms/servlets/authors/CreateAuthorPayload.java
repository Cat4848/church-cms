package church.cms.servlets.authors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAuthorPayload(
        @NotNull @Size(min = 3, max = 100, message = "Author name is invalid. Must be between 3 and 100 characters.")
        String name) {
}
