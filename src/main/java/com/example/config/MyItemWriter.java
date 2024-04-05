package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class MyItemWriter implements ItemWriter<String> {
	public static final Logger logger = LoggerFactory.getLogger(MyItemWriter.class);

	@Override
	public void write(Chunk<? extends String> item) throws Exception {
		logger.info("Writing item: {}", item);
	}
}
