package uz.tsue.ricoin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.dto.UserDto;
import uz.tsue.ricoin.exceptions.UserAccountException;
import uz.tsue.ricoin.repository.UserRepository;
import uz.tsue.ricoin.service.interfaces.UserService;
import uz.tsue.ricoin.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser(@AuthenticationPrincipal User user) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getCurrentUser(user));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> allUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/add-balance")
    public ResponseEntity<?> addBalance(@PathVariable Long id, @RequestParam int balance, HttpServletRequest request) {
        userService.addBalance(userService.findById(id), balance);
        return ResponseEntity.ok(notificationService.generateNotificationMessage("application.notification.Successfully", request));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user, HttpServletRequest request) {
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.generateCreatedNotificationMessage(request));


    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, HttpServletRequest request) {
        User user = new User();
        user.setId(id);
        userService.deleteUser(user, request);
        return ResponseEntity.ok().build();

    }

}










