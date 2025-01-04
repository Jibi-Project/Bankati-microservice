package com.example.notifications_server.service;

import com.example.notifications_server.model.Notification;
import com.example.notifications_server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Log the email notification to the database
    public Notification logNotification(Notification notification) {
        notification.setCreatedAt(new Date());
        return notificationRepository.save(notification);
    }

    // Retrieve all notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
