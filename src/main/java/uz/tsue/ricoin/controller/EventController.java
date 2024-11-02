package uz.tsue.ricoin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tsue.ricoin.dto.EventDto;
import uz.tsue.ricoin.service.NotificationService;
import uz.tsue.ricoin.service.interfaces.EventService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("admin/event")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class EventController {

    private final EventService eventService;
    private final NotificationService notificationService;

    @GetMapping("/{id}/get")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.get(id));
    }

    @GetMapping("/all/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EventDto eventDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.create(eventDto));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EventDto eventDto, HttpServletRequest request) {
        eventService.update(id, eventDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateUpdatedNotificationMessage(request));
    }

    @PostMapping("/{id}/code/update")
    public ResponseEntity<?> updateCode(@PathVariable Long id, HttpServletRequest request) {
        eventService.updatePromoCode(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateUpdatedNotificationMessage(request));
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable Long id, HttpServletRequest request) {
        eventService.remove(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.generateRemovedNotificationMessage(request));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notificationService.generateNotificationMessage("application.exception.notification.EventNotFound", request));
    }


}

