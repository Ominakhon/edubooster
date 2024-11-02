package uz.tsue.ricoin.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import uz.tsue.ricoin.dto.response.OrderResponseDto;
import uz.tsue.ricoin.entity.Order;
import uz.tsue.ricoin.entity.User;

import java.util.List;

public interface OrderService {
    void save(Order order);

    OrderResponseDto makeOrder(User user, Long id, int quantity);

    void cancelOrder(Long orderId, HttpServletRequest request);

    OrderResponseDto get(Long id);

    List<OrderResponseDto> getAll(Long userId);

    Order find(Long id);

    void remove(Long id);

    void approveOrder(Long orderId, HttpServletRequest request);
}
