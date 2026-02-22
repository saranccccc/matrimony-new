package com.matrimony.notification.repository;

import com.matrimony.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Long countByUserIdAndIsReadFalse(String userId);

    @Modifying
    @Query("""
            update Notification n
            set n.isRead = true
            where n.id = :id
            """)
    void markAsRead(@Param("id") Long id);

    @Modifying
    @Query("""
            update Notification n
            set n.isRead = true
            where n.userId = :userId
            and n.isRead = false
            """)
    void markAllAsRead(@Param("userId") String userId);
}
