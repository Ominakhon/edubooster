package uz.tsue.ricoin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.tsue.ricoin.entity.User;
import uz.tsue.ricoin.service.NotificationService;
import uz.tsue.ricoin.service.interfaces.OrderService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @PostMapping("/{productId}/order")
    public ResponseEntity<?> orderProduct(@AuthenticationPrincipal User user, @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.makeOrder(user, productId, quantity));
    }


    @GetMapping("/order/all/get")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAll(user.getId()));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/order/{id}/remove")
    public ResponseEntity<?> removeOrder(@PathVariable Long id, HttpServletRequest request) {
        orderService.remove(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateRemovedNotificationMessage(request));
    }

    @DeleteMapping("order/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, HttpServletRequest request) {
        orderService.cancelOrder(orderId, request);
        return ResponseEntity.ok()
                .body(notificationService.generateNotificationMessage("application.notification.OrderCancelled", request));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("order/{orderId}/approve")
    public ResponseEntity<?> approveOrder(@PathVariable Long orderId, HttpServletRequest request) {
        orderService.approveOrder(orderId, request);
        return ResponseEntity.ok(notificationService.generateRemovedNotificationMessage(request));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notificationService.generateNotificationMessage("application.exception.notification.EventNotFound", request));
    }


}
