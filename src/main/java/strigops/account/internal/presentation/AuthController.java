package strigops.account.internal.presentation;

import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.register.UserRegistrationService;
import strigops.account.features.register.command.CreateUserCommand;
import strigops.account.features.register.command.UserRegistrationResult;
import strigops.account.features.register.dto.RegisterUserRequest;
import strigops.account.features.register.dto.RegisterUserResponse;

@Path("/v1/register")
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRegistrationService registrationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterUserRequest request) {
        log.info("Register request received");

        var command = new CreateUserCommand(
                request.email().trim().toLowerCase(),
                request.password(),
                request.username()
        );

        UserRegistrationResult result = registrationService.register(command);
        log.info("User registration successful with id={}", result.userId());

        return Response.status(Response.Status.CREATED)
                .entity(new RegisterUserResponse(result.userId(), result.email()))
                .build();
    }
}
