package com.internship.driverservice.repo;

import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByRideId(String rideId);
    Optional<Notification> findByRideIdAndStatusAndType(String rideId, NotificationStatus status, NotificationType type);
}
