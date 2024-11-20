package uz.tsue.ricoin.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import uz.tsue.ricoin.dto.response.UserDto;
import uz.tsue.ricoin.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User findById(Long id);

    List<UserDto> findAll();

    void save(User user);

//    void save(User user, HttpServletRequest request);


    boolean hasEnoughBalance(int amount, User user);

    void debitBalance(User user, int amount);

    void addBalance(User user, int amount);

    UserDto getCurrentUser(User user);


//    void update(UserDto userDto);

    void update(UserDto userDto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);
}
