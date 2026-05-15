package church.cms.servlets.authors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateAuthorPayload(@NotNull @Positive Integer authorId,
                                  @NotNull @Size(min = 3,
                                          max = 100,
                                          message = "Author name invalid on update Author.  Must be between 3 and 100 characters.")
                                  String name) {
}