
package com.example.rental_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.rental_platform.model.Notification;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    // =========================
    // GET ALL NOTIFICATIONS OF A USER
    // =========================
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    // =========================
    // GET UNREAD NOTIFICATIONS
    // =========================
    List<Notification> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long recipientId);

    // =========================
    // COUNT UNREAD NOTIFICATIONS
    // =========================
    long countByRecipientIdAndIsReadFalse(Long recipientId);


    // =========================
// MARK ALL NOTIFICATIONS AS READ
// =========================
@Transactional
@Modifying
@Query("""
       UPDATE Notification n
       SET n.isRead = true
       WHERE n.recipient.id = :recipientId
       AND n.isRead = false
       """)
int markAllAsRead(Long recipientId);

}