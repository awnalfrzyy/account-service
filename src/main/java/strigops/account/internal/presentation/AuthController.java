package strigops.account.internal.presentation;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.auth.changePassword.ChangePasswordService;
import strigops.account.features.auth.changePassword.dto.ChangePasswordRequest;
import strigops.account.features.auth.login.LoginService;
import strigops.account.features.auth.login.command.LoginCommand;
import strigops.account.features.auth.login.command.LoginResult;
import strigops.account.features.auth.login.dto.LoginRequest;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.auth.login.dto.SendOtpRequest;
import strigops.account.features.auth.login.dto.VerifyOtpRequest;
import strigops.account.features.auth.register.UserRegistrationService;
import strigops.account.features.auth.register.command.CreateUserCommand;
import strigops.account.features.auth.register.command.UserRegistrationResult;
import strigops.account.features.auth.register.dto.RegisterUserRequest;
import strigops.account.features.auth.register.dto.RegisterUserResponse;
import strigops.account.internal.infrastructure.config.OtpService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRegistrationService registrationService;
    private final LoginService loginService;
    private final OtpService otpService;
    private final ChangePasswordService changePasswordService;

    @POST
    @Path("/v1/send-otp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendOtp(@Valid SendOtpRequest request) {
        log.info("Send OTP request for email: {}", request.email());

        String otp = otpService.generateOtp(request.email());
        otpService.sendOtpEmail(request.email(), otp);

        return Response.ok("{\"message\": \"OTP sent to email\"}").build();
    }

    @POST
    @Path("/v1/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterUserRequest request) {
        log.info("Register request received");

        if (!otpService.isEmailVerified(request.email())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Email must be verified via OTP first\"}")
                    .build();
        }

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

    @POST
    @Path("/v1/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyOtp(@Valid VerifyOtpRequest request) {
        log.info("Verify OTP request for email: {}", request.email());

        boolean isValid = otpService.verifyOtp(request.email(), request.otp());
        if (isValid) {
            return Response.ok("{\"message\": \"OTP verified successfully\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid or expired OTP\"}")
                    .build();
        }
    }

    @POST
    @Path("/v1/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest request, @Context HttpServletResponse response) {
        log.info("Login request received for email: {}", request.email());

        if (!otpService.isEmailVerified(request.email())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Email must be verified via OTP first\"}")
                    .build();
        }

        var command = new LoginCommand(request.email().trim().toLowerCase(), request.password());
        LoginResult result = loginService.login(command);

        Cookie cookie = new Cookie("jwtToken", result.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        log.info("User login successful for email: {}", result.getEmail());

        return Response.ok(new LoginResponse(result.getUserId(), result.getEmail(), result.getToken())).build();
    }

    @PUT
    @Path("/v1/changePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@Valid ChangePasswordRequest request) {
        log.info("Change password request received for email:{}", request.email());

        changePasswordService.changePassword(request);
        log.info("Password successfully changed for email: {}", request.email());
        return Response.noContent().build();
    }

    @POST
    @Path("/v1/logout")
    public Response logout(@Context HttpServletResponse response) {
        log.info("Logout request received");

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        log.info("User logged out successfully");
        return Response.ok("{\"message\": \"Logged out successfully\"}").build();
    }

}
