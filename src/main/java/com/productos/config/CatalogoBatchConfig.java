package com.productos.config;

import java.nio.file.Path;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.productos.catalogo.batch.UsuariosItemProcessor;
import com.productos.catalogo.batch.UsuariosItemWriter;
import com.productos.catalogo.pdf.PdfEnvioResultado;
import com.productos.catalogo.pdf.PdfGenerateService;
import com.productos.model.Email;
import com.productos.model.Usuario;
import com.productos.service.EmailService;
import com.productos.service.ProductService;
import com.productos.service.EnviarEmailService;

@Configuration
@EnableJdbcJobRepository
public class CatalogoBatchConfig {
	
	@Autowired
	private PdfGenerateService pdfGenerateService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EnviarEmailService sendEmailService;
	
	@Bean
	UsuariosItemWriter catalogoItemWriter() {
		return new UsuariosItemWriter();
	}
	
	@Bean
	public TaskExecutor catalogoTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(1);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setQueueCapacity(5);
		taskExecutor.initialize();
		return taskExecutor;
	}
	
	@Bean
	public Step enviarPdfStep(JobRepository jobRepository,
			PlatformTransactionManager transactionManager,
			RepositoryItemReader<Usuario> usuarioReader,
			UsuariosItemProcessor itemProcessor,
			ItemWriter<PdfEnvioResultado> logWriter) {
		return new StepBuilder("enviarPdfStep", jobRepository)
				.<Usuario, PdfEnvioResultado>chunk(50).transactionManager(transactionManager)
				.reader(usuarioReader)
				.processor(itemProcessor)
				.writer(logWriter)
				.listener(itemProcessor)
				.build();
	}
	
	@Bean
	public Step enviarEmailsStep(JobRepository jobRepository,
			PlatformTransactionManager transactionManager,
			EmailService emailService,
			EnviarEmailService sendEmailService) {
		return new StepBuilder("enviarEmailsStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				int limit = 100;
					
				List<Email> batch = emailService.claimPending(limit);
				if(batch.isEmpty()) return RepeatStatus.FINISHED;
				
				for(Email email : batch) {
					try {
						sendEmailService.enviarEmailConAdjunto(
								email.getRecipient(),
								email.getSubject(),
								email.getBody(),
								Path.of(email.getAttachmentPath())
								);
						emailService.markSent(email.getId(), null);
					}catch(Exception e) {
						emailService.markFailed(email.getId(), e.getMessage());
					}
				}
				return RepeatStatus.CONTINUABLE;
		}, transactionManager)
		.build();
	}
	
	@Bean(name = "enviarPdfJob")
	public Job enviarPdfJob(JobRepository jobRepository, Step enviarPdfStep) {
		return new JobBuilder("enviarPdfJob", jobRepository)
				.start(enviarPdfStep)
				.build();
	}
	
	@Bean(name = "enviarEmailsJob")
	public Job enviarEmailsJob(JobRepository jobRepository, Step enviarEmailsStep) {
		return new JobBuilder("enviarEmailsJob", jobRepository)
				.start(enviarEmailsStep)
				.build();
	}
}
