package uz.tsue.ricoin.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AuthenticationResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int balance;
    private String groupName;
    private boolean isActive;
    private String verificationCode;
    private LocalDateTime verificationCodeExpirationTime;
}
