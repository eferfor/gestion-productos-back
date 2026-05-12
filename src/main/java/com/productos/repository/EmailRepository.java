package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.productos.model.Email;
import com.productos.model.EmailStatus;

public interface EmailRepository extends JpaRepository<Email, Long>{

	@Modifying
	@Query("""
			update Email e
				set e.status = com.productos.model.EmailStatus.SENDING
			where e.id in :ids
				and e.status = com.productos.model.EmailStatus.PENDING
			""")
	int markSending(@Param("ids") List<Long> ids);
	
	List<Email> findTop100ByStatusOrderByCreatedAtAsc(EmailStatus status);
	
}
