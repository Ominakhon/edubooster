package uz.tsue.ricoin.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final MessageSource messageSource;

    public String generateNotificationMessage(String message, HttpServletRequest request) {
        return messageSource.getMessage(message, null, RequestContextUtils.getLocale(request));
    }

    public String generateCreatedNotificationMessage(HttpServletRequest request) {
        return messageSource.getMessage("application.notification.Created", null, RequestContextUtils.getLocale(request));
    }


    public String generateUpdatedNotificationMessage(HttpServletRequest request) {
        return messageSource.getMessage("application.notification.Updated", null, RequestContextUtils.getLocale(request));
    }

    public String generateRemovedNotificationMessage(HttpServletRequest request) {
        return messageSource.getMessage("application.notification.Removed", null, RequestContextUtils.getLocale(request));
    }



}
