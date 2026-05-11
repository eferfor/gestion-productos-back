package com.productos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.model.Email;
import com.productos.model.EmailStatus;
import com.productos.repository.EmailRepository;

@Service
public class EmailService {

	@Autowired
	private EmailRepository repo;
	
	public Email createPendingEmail(Long userId, String recipient, String subject, String body, String attachmentPath) {
		Email item = Email.builder()
				.userId(userId)
				.recipient(recipient)
				.subject(subject)
				.body(body)
				.attachmentPath(attachmentPath)
				.status(EmailStatus.PENDING)
				.attempts(0)
				.lastError(null)
				.sentAt(null)
				.build();
		
		return repo.save(item);
	}
	
}
