package com.safepay.safeai.configurations;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingModelConfig {
	@Bean
	public EmbeddingModel embeddingModel(OpenAiEmbeddingModel openAiEmbeddingModel) {
		return openAiEmbeddingModel;
	}
}
