package com.xsakon.notification.rabbitmq;

import com.xsakon.clients.notification.NotificationRequest;
import com.xsakon.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    public void consume(NotificationRequest notificationRequest){
        log.info("Consumed {} from queue", notificationRequest);

        notificationService.send(notificationRequest);
    }

}
