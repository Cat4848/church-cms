package church.cms.servlets.hymnBooks;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHymnBookPayload(
        @NotNull @Size(min = 3, max = 100, message = "Hymn Book name is invalid on create Hymn Book. Must be between 3 and 100 characters.")
        String name
) {
}