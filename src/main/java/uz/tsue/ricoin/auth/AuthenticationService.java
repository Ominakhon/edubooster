package uz.tsue.ricoin.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;
import uz.tsue.ricoin.dto.request.UserLoginRequestDto;
import uz.tsue.ricoin.dto.request.UserVerificationRequestDto;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.entity.VerificationCode;
import uz.tsue.ricoin.entity.enums.Role;
import uz.tsue.ricoin.exceptions.InvalidRequestException;
import uz.tsue.ricoin.exceptions.UserAccountException;
import uz.tsue.ricoin.repository.UserRepository;
import uz.tsue.ricoin.repository.VerificationCodeRepository;
import uz.tsue.ricoin.service.EmailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;

    private final MessageSource messageSource;

    public AuthenticationResponse signUp(AuthenticationRequest userDto, HttpServletRequest request) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAccountException(
                    messageSource.getMessage("application.exception.notification.EmailExists",
                            new Object[]{userDto.getEmail()},
                            RequestContextUtils.getLocale(request))
            );
        }

        VerificationCode code = VerificationCode.builder()
                .code(generateVerificationCode(6))
                .expiresAt(LocalDateTime.now().plusMinutes(1))
                .build();

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .balance(0)
                .groupName(userDto.getGroupName())
                .role(Role.USER)
                .isActive(false)
                .verificationCode(code)
                .build();

        sendVerificationEmail(user, request);
        verificationCodeRepository.save(code);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .balance(user.getBalance())
                .groupName(user.getGroupName())
                .isActive(user.isActive())
                .verificationCode(user.getVerificationCode().getCode())
                .verificationCodeExpirationTime(user.getVerificationCode().getExpiresAt())
                .build();
    }

    public User authenticate(UserLoginRequestDto userDto, HttpServletRequest request) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserAccountException(
                        messageSource.getMessage("application.exception.notification.UserNotFound", null, RequestContextUtils.getLocale(request))
                ));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UserAccountException(
                    messageSource.getMessage("application.exception.notification.BadCredentials", null, RequestContextUtils.getLocale(request)));
        }

        if (!user.isActive()) {
            throw new UserAccountException(
                    messageSource.getMessage("application.exception.notification.AccountNotVerified", null, RequestContextUtils.getLocale(request)));
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        return user;
    }

    public void verifyUser(UserVerificationRequestDto userDto, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode().getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new InvalidRequestException(
                        messageSource.getMessage("application.exception.notification.VerificationCodeExpired", null, RequestContextUtils.getLocale(request)));
            }
            if (user.getVerificationCode().getCode().equals(userDto.getVerificationCode())) {
                user.setActive(true);
                user.getVerificationCode().setCode(null);
                user.getVerificationCode().setExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new InvalidRequestException(
                        messageSource.getMessage("application.exception.notification.InvalidVerificationCode", null, RequestContextUtils.getLocale(request)));
            }
        } else {
            throw new UserAccountException(
                    messageSource.getMessage("application.exception.notification.UserNotFound", null, RequestContextUtils.getLocale(request)));
        }
    }

    public void resendVerificationCode(String email, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isActive()) {
                throw new InvalidRequestException(
                        messageSource.getMessage("application.exception.notification.AccountAlreadyVerified", null, RequestContextUtils.getLocale(request)));
            }
            user.getVerificationCode().setCode(generateVerificationCode(6));
            user.getVerificationCode().setExpiresAt(LocalDateTime.now().plusMinutes(30));
            sendVerificationEmail(user, request);
            userRepository.save(user);
        } else {
            throw new UserAccountException(
                    messageSource.getMessage("application.exception.notification.UserNotFound", null, RequestContextUtils.getLocale(request)));
        }
    }

    public void sendVerificationEmail(User user, HttpServletRequest request) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode().getCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new InvalidRequestException(messageSource.getMessage("application.exception.notification.FailedToSendCode", null, RequestContextUtils.getLocale(request)));
        }
    }

    private String generateVerificationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }


}
















