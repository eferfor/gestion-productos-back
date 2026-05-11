package com.productos.config;

import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.productos.batch.service.ProductBatchService;
import com.productos.batch.steps.ProductsItemProcessor;
import com.productos.batch.steps.ProductsItemWriter;
import com.productos.dto.ProductDTO;
import com.productos.exception.ProductNotFoundForUpdateException;

@Configuration
@EnableJdbcJobRepository
public class BatchConfig {

	@Autowired
	private ProductBatchService batchService;

	@Bean
	ProductsItemWriter itemWriter() {
		return new ProductsItemWriter(batchService);
	}
	
	@Bean
	ProductsItemProcessor itemProcessor(){
		return new ProductsItemProcessor();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(1);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setQueueCapacity(5);
		taskExecutor.initialize();
		return taskExecutor;
	}
	
	@Bean
	public Step readFile(JobRepository jobRepository,
			PlatformTransactionManager transactionManager,
			ItemReader<ProductDTO> itemReader,
			ItemProcessor<ProductDTO, ProductDTO> itemProcessor,
			ItemWriter<ProductDTO> itemWriter) {
		return new StepBuilder("agregarProductos", jobRepository)
				.<ProductDTO, ProductDTO>chunk(50).transactionManager(transactionManager)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.faultTolerant()
				.skip(ProductNotFoundForUpdateException.class)
				.skipLimit(3)
				.build();
	}
	
	@Bean(name = "agregarProductosJob")
	public Job job(JobRepository jobRepository, Step readFile) {
		return new JobBuilder("agregarProductos", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(readFile)
				.build();
	}
	
	@Bean(name = "customJobOperator")
	public JobOperatorFactoryBean customJobOperator(JobRepository jobRepository, TaskExecutor taskExecutor) throws Exception{
		JobOperatorFactoryBean factory = new JobOperatorFactoryBean();
		factory.setJobRepository(jobRepository);
		factory.setTaskExecutor(new SyncTaskExecutor());
		return factory;
	}
	
}
