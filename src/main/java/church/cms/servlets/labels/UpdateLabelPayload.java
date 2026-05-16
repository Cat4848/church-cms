package church.cms.servlets.labels;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateLabelPayload(@NotNull @Positive Integer labelId,
                                 @NotNull @Size(min = 3,
                                         max = 100,
                                         message = "Label name invalid on update Label.  Must be between 3 and 100 characters.")
                                 String name) {
}