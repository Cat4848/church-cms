package church.cms.servlets.labels;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLabelPayload(
        @NotNull @Size(min = 3, max = 100, message = "Label name is invalid on create Label. Must be between 3 and 100 characters.")
        String name
) {
}