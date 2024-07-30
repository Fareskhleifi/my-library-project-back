package com.library.booklend.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinateur_id", nullable = false)
    private Utilisateur destinateur;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_notification", nullable = false)
    private Date dateNotification;

    @Column(name = "statut_lecture", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean statutLecture;

    // Getters and setters (Omitted for brevity)
}
