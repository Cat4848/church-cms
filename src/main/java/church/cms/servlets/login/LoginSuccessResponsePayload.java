package church.cms.servlets.login;

public record LoginSuccessResponsePayload(
        String firstName,
        String lastName,
        String email,
        Boolean isAdmin
) {
}