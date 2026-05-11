package church.cms.servlets.hymnBooks;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateHymnBookPayload(@NotNull @Positive Integer hymnBookId,
                                    @NotNull @Size(min = 3,
                                            max = 100,
                                            message = "Hymn Book name invalid on update Hymn Book.  Must be between 3 and 100 characters.")
                                    String name) {
}