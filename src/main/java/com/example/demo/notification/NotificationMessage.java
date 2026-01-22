package com.example.demo.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for WebSocket notification messages
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {
    
    private String type; // e.g., "RESULT_PUBLISHED", "PHASE_CHANGED"
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private Object data; // Additional data if needed
    
    public static NotificationMessage resultPublished(String typeList) {
        return NotificationMessage.builder()
                .type("RESULT_PUBLISHED")
                .title("Résultats publiés")
                .message("La liste " + (typeList.equals("principale") ? "principale" : "d'attente") + " a été publiée.")
                .timestamp(LocalDateTime.now())
                .data(typeList)
                .build();
    }
    
    public static NotificationMessage phaseChanged(String phaseName, String status) {
        return NotificationMessage.builder()
                .type("PHASE_CHANGED")
                .title("Changement de phase")
                .message("La phase \"" + phaseName + "\" est maintenant " + status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
