package church.cms.servlets.topics;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateTopicPayload(@NotNull @Positive Integer topicId,
                                 @NotNull @Size(min = 3,
                                         max = 100,
                                         message = "Topic name invalid on update Topic.  Must be between 3 and 100 characters.")
                                 String name) {
}