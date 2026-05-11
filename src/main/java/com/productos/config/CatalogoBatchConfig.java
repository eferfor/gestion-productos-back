package com.productos.config;

import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.productos.catalogo.PdfEnvioResultado;
import com.productos.catalogo.PdfGenerateService;
import com.productos.catalogo.batch.UsuariosItemProcessor;
import com.productos.catalogo.batch.UsuariosItemWriter;
import com.productos.model.Usuario;
import com.productos.service.ProductService;

@Configuration
@EnableJdbcJobRepository
public class CatalogoBatchConfig {
	
	@Autowired
	private PdfGenerateService pdfGenerateService;
	
	@Autowired
	private ProductService productService;
	
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
	
	@Bean(name = "enviarPdfJob")
	public Job enviarPdfJob(JobRepository jobRepository, Step enviarPdfStep) {
		return new JobBuilder("enviarPdfJob", jobRepository)
				.start(enviarPdfStep)
				.build();
	}
	
}
