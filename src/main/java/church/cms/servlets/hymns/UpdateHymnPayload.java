package church.cms.servlets.hymns;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateHymnPayload(
        @NotNull @Positive
        Integer hymnId,

        @NotNull @Positive
        Integer authorId,

        @Size(max = 200, message = "Hymn Author Extras in invalid on Create Hymn. Must be no more than 200 characters.")
        String authorExtras,

        @NotNull @Size(max = 100, message = "Hymn Title in invalid on Create Hymn. Must be no more than 100 characters.")
        String title,

        @NotNull @Size(min = 3, max = 21000, message = "Hymn Lyrics in invalid on Create Hymn. Must be between 3 and 21,000 characters.")
        String lyrics,

        @Positive
        Integer hymnBookId,

        @Positive
        Integer numberInHymnBook,

        @Positive
        Integer topicId,

        @Positive
        Integer labelId
) {
}