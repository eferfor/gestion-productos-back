package com.productos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "email")
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	private String recipient;
	private String subject;
	
	@Column(name = "attachment_path")
	private String attachmentPath;
	
	@Column(columnDefinition = "TEXT")
	private String body;
	
	@Enumerated(EnumType.STRING)
	private EmailStatus status;

	private int attempts;
	
	@Column(name = "last_error", columnDefinition = "TEXT")
	private String lastError;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "sent_at")
	private LocalDateTime sentAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
	
}
