package com.productos.batch;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class BatchController {

	private final JobOperator jobOperator;
	private final JobRepository jobRepository;
	private final Job agregarProductosJob;
	
	public BatchController(@Qualifier("customJobOperator") JobOperator jobOperator, JobRepository jobRepository, Job agregarProductosJob) {
		this.jobOperator = jobOperator;
		this.jobRepository = jobRepository;
		this.agregarProductosJob = agregarProductosJob;
	}
	
	@PostMapping("/uploadFile")
	public ResponseEntity<?> receiveFile(@RequestParam(name = "file") MultipartFile multipartFile){
		String fileName = multipartFile.getOriginalFilename();
		
		if(fileName == null || fileName.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Nombre de archivo inválido"));
		}
		
		if(!fileName.toLowerCase().endsWith(".csv")) {
			return ResponseEntity.badRequest().body("Sólo se permiten CSV");
		}
		
		
		try {
			Path uploadDir = Paths.get("uploads");
			Files.createDirectories(uploadDir);
			
			Path storedPath = uploadDir.resolve(fileName);
			Files.copy(multipartFile.getInputStream(), storedPath, StandardCopyOption.REPLACE_EXISTING);
			
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("filePath", storedPath.toAbsolutePath().toString())
					.addLong("run.id ", System.currentTimeMillis())
					.toJobParameters();
			
			
			JobExecution execution = jobOperator.run(agregarProductosJob, jobParameters);
			
			Map<String, String> response = new HashMap<>();
			response.put("archivo", fileName);
			response.put("estado", "recibido");
			response.put("Job lanzado. ExecutionId = ", String.valueOf(execution.getId()));
			response.put("status = ", execution.getStatus().toString());
			
			return ResponseEntity.ok(response);
			
		}catch (Exception e) {
			log.error("Error al iniciar el batch, Error {}", e.getMessage());
			throw new RuntimeException();
		}
	}
	
}
