package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfig {

	public static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	private static final int BATCH_SIZE = 3;
	final JobRepository jobRepository;
	final PlatformTransactionManager batchTransactionManager;

	public BatchConfig(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager) {
		this.jobRepository = jobRepository;
		this.batchTransactionManager = batchTransactionManager;
	}

	/**
	 * Job which contains multiple steps
	 */
	@Bean
	public Job firstJob() {
		return new JobBuilder("first job", jobRepository).incrementer(new RunIdIncrementer())
				.start(chunkStep())
				.next(taskletStep())
				.build();
	}

	@Bean
	public Step taskletStep() {
		return new StepBuilder("first step", jobRepository).tasklet((stepContribution, chunkContext) -> {
					logger.info("This is first tasklet step");
					logger.info("SEC = {}", chunkContext.getStepContext()
							.getStepExecutionContext());
					return RepeatStatus.FINISHED;
				}, batchTransactionManager)
				.build();
	}

	@Bean
	public Step chunkStep() {
		return new StepBuilder("first step", jobRepository).<String, String>chunk(BATCH_SIZE, batchTransactionManager)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public ItemReader<String> reader() {
		return new MyItemReader();
	}

	@Bean
	public ItemProcessor<String, String> processor() {
		return new MyItemProcessor();
	}

	@Bean
	public ItemWriter<String> writer() {
		return new MyItemWriter();
	}

}