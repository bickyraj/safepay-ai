package com.safepay.safeai.configurations;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatConfig {

	private final OpenAiChatModel chatModel;

	@Bean
	ChatClient chatClient(SyncMcpToolCallbackProvider toolCallbackProvider) {
		return ChatClient
				.builder(chatModel)
				.defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
				.build();
	}
}
