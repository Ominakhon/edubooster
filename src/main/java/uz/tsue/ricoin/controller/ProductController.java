package uz.tsue.ricoin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tsue.ricoin.dto.ProductDto;
import uz.tsue.ricoin.service.NotificationService;
import uz.tsue.ricoin.service.interfaces.ProductService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("admin/product")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {
    private final ProductService productService;
    private final NotificationService notificationService;

    @GetMapping("/{id}/get")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.get(id));
    }

    @GetMapping("/all/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(productDto));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDto productDto, HttpServletRequest request) {
        productService.update(id, productDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateUpdatedNotificationMessage(request));
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable Long id, HttpServletRequest request) {
        productService.remove(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateRemovedNotificationMessage(request));
    }


    @PostMapping("/{id}/add-quantity")
    public ResponseEntity<?> addQuantity(@PathVariable Long id, @RequestParam int quantity, HttpServletRequest request) {
        productService.addProductQuantity(productService.findById(id), quantity);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateNotificationMessage("application.notification.Successfully", request));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notificationService.generateNotificationMessage("application.exception.notification.ProductNotFound", request));
    }
}
