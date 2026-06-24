package com.safepay.safeai.configurations;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatConfig {

	@Bean
	@Primary
	ChatModel chatModel(OllamaChatModel ollamaChatModel) {
		return ollamaChatModel;
	}

	@Bean("myOpenAiChatModel")
	ChatModel openAiChatModel (OpenAiChatModel openAiChatModel) {
		return openAiChatModel;
	}

	@Bean
	ChatClient chatClient(@Qualifier("myOpenAiChatModel") ChatModel chatModel,
						  SyncMcpToolCallbackProvider toolCallbackProvider, ChatMemory chatMemory) {
		return ChatClient
				.builder(chatModel)
				.defaultSystem("You are a helpful IMS assistant.")
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				.defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
				.build();
	}
}
