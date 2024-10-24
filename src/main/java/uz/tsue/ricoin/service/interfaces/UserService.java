package uz.tsue.ricoin.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.dto.UserDto;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User findById(Long id);

    List<UserDto> findAll();

    void save(User user);

    boolean hasEnoughBalance(int amount, User user);

    void debitBalance(User user, int amount);

    void addBalance(User user, int amount);

    UserDto getCurrentUser(User user);


    void updateUser(UserDto userDto);

    void deleteUser(User user, HttpServletRequest request);
}
