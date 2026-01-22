package com.example.demo.notification;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.model.Notification;
import com.example.demo.candidat.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for sending real-time notifications via WebSocket
 * and persisting them to the database
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    /**
     * Broadcast a notification to all connected clients
     */
    public void broadcastToAll(NotificationMessage notification) {
        log.info("Broadcasting notification to all: type={}, title={}", 
                notification.getType(), notification.getTitle());
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    /**
     * Send a notification to a specific user
     */
    public void sendToUser(String userId, NotificationMessage notification) {
        log.info("Sending notification to user {}: type={}", 
                userId, notification.getType());
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }

    /**
     * Broadcast result publication to all candidates and persist to database
     */
    @Transactional
    public void notifyResultPublication(String typeList, List<Candidat> affectedCandidats) {
        // Create websocket notification
        NotificationMessage wsNotification = NotificationMessage.resultPublished(typeList);
        
        // Broadcast to all connected clients
        broadcastToAll(wsNotification);
        
        // Also send to specific topic for candidates
        messagingTemplate.convertAndSend("/topic/candidats/results", wsNotification);
        
        // Persist notification for each affected candidate with enhanced fields
        for (Candidat candidat : affectedCandidats) {
            try {
                Notification notification = Notification.builder()
                        .type("RESULT_" + (typeList.equals("principale") ? "LP" : "LA") + "_PUBLISHED")
                        .typeNotification("RESULT_" + (typeList.equals("principale") ? "LP" : "LA"))
                        .candidat(candidat)
                        .message(typeList.equals("principale") 
                                ? "Félicitations ! Vous êtes admis sur la Liste Principale." 
                                : "Vous êtes sur la Liste d'Attente.")
                        .lue(false)
                        .dateCreation(java.time.LocalDateTime.now())
                        .build();
                
                notificationRepository.save(notification);
            } catch (Exception e) {
                log.error("Failed to save notification for candidat {}: {}", 
                        candidat.getId(), e.getMessage());
            }
        }
        
        log.info("Notified {} candidates about {} publication", 
                affectedCandidats.size(), typeList);
    }

    /**
     * Notify about phase change
     */
    public void notifyPhaseChange(String phaseName, String status) {
        NotificationMessage notification = NotificationMessage.phaseChanged(phaseName, status);
        broadcastToAll(notification);
    }
}
