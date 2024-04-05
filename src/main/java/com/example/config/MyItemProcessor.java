package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class MyItemProcessor implements ItemProcessor<String, String> {
	public static final Logger logger = LoggerFactory.getLogger(MyItemProcessor.class);

	@Override
	public String process(String item) throws Exception {
		logger.info("processing item: {}", item);
		return item.toUpperCase();
	}
}
