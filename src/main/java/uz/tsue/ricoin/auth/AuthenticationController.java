package uz.tsue.ricoin.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tsue.ricoin.dto.request.UserLoginRequestDto;
import uz.tsue.ricoin.dto.request.UserVerificationRequestDto;
import uz.tsue.ricoin.dto.response.UserLoginResponseDto;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.service.JwtService;
import uz.tsue.ricoin.service.NotificationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationRequest userDto, HttpServletRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.signUp(userDto, request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> authenticate(@RequestBody UserLoginRequestDto userDto, HttpServletRequest request) {
        User user = authenticationService.authenticate(userDto, request);
        String jwt = jwtService.generateToken(user).getToken();
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(jwt, jwtService.getExpirationTime());
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody UserVerificationRequestDto userDto, HttpServletRequest request) {
        authenticationService.verifyUser(userDto, request);
        return ResponseEntity.ok(notificationService.generateNotificationMessage("application.notification.UserAccountVerified", request));
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        authenticationService.resendVerificationCode(email, request);
        return ResponseEntity.ok(notificationService.generateNotificationMessage("application.notification.AccountVerificationCodeSent", request));
    }

}





