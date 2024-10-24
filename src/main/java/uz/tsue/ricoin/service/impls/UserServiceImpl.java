package uz.tsue.ricoin.service.impls;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.exceptions.UserAccountException;
import uz.tsue.ricoin.repository.UserRepository;
import uz.tsue.ricoin.dto.UserDto;
import uz.tsue.ricoin.service.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final MessageSource messageSource;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> users = new ArrayList<>();
        for (User user : userList) {
            users.add(
                    UserDto.builder()
                            .email(user.getEmail())
                            .phoneNumber(user.getPhoneNumber())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .groupName(user.getGroupName())
                            .balance(user.getBalance())
                            .createdDate(user.getCreatedDate())
                            .lastModifiedDate(user.getLastModifiedDate())
                            .orders(user.getOrders())
                            .build()
            );
        }
        return users;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean hasEnoughBalance(int productPrice, User user) {
        return user.getBalance() >= productPrice;
    }

    @Override
    public void debitBalance(User user, int amount) {
        user.setBalance(user.getBalance() - amount);
    }

    @Override
    public void addBalance(User user, int amount) {
        user.setBalance(user.getBalance() + amount);
        save(user);
    }

    @Override
    public UserDto getCurrentUser(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .groupName(user.getGroupName())
                .phoneNumber(user.getPhoneNumber())
                .balance(user.getBalance())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .orders(user.getOrders())
                .build();
    }


    @Override
    public void updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(RuntimeException::new);
        Optional.ofNullable(userDto.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userDto.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(userDto.getGroupName()).ifPresent(user::setGroupName);
        Optional.ofNullable(userDto.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user, HttpServletRequest request) {
        User userToDelete = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserAccountException(
                        messageSource.getMessage("application.exception.notification.UserNotFound", null, RequestContextUtils.getLocale(request))
                ));

        userRepository.delete(userToDelete);
    }
}

