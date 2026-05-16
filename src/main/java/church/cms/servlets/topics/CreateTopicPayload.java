package church.cms.servlets.topics;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTopicPayload(
        @NotNull @Size(min = 3, max = 100, message = "Topic name is invalid on create Topic. Must be between 3 and 100 characters.")
        String name
) {
}