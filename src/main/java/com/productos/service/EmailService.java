package com.productos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.model.Email;
import com.productos.model.EmailStatus;
import com.productos.repository.EmailRepository;

import jakarta.transaction.Transactional;

@Service
public class EmailService {

	private final EmailRepository repo;
	
	public EmailService(EmailRepository repo) {
		this.repo = repo;
	}
	
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

	@Transactional
	public List<Email> claimPending(int limit){
		List<Email> pending = repo.findTop100ByStatusOrderByCreatedAtAsc(EmailStatus.PENDING);
		if(pending.isEmpty()) return pending;
		
		List<Long> ids = pending.stream().map(Email::getId).toList();
		repo.markSending(ids);
		return pending;
	}
	
	@Transactional
	public void markSent(Long id, String providerId) {
		Email e = repo.findById(id).orElseThrow();
		e.setStatus(EmailStatus.SENT);
		e.setSentAt(LocalDateTime.now());
		e.setLastError(null);
		repo.save(e);
	}
	
	@Transactional
	public void markFailed(Long id, String error) {
		Email e = repo.findById(id).orElseThrow();
		e.setStatus(EmailStatus.FAILED);
		e.setAttempts(e.getAttempts() + 1);
		e.setLastError(error);
		repo.save(e);
	}
}
