package com.safepay.safeai.services;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {
	private final VectorStore vectorStore;
	private final ChatClient chatClient;

	public RagService(VectorStore vectorStore, ChatClient.Builder chatClient) {
		this.vectorStore = vectorStore;
		this.chatClient = chatClient.build();
	}

	public String ask(String question) {

		List<Document> context = vectorStore.similaritySearch(question);

		String contextText = context.stream()
				.map(Document::getFormattedContent)
				.reduce("", (a, b) -> a + "\n" + b);

		return chatClient.prompt()
				.system("Answer only using the provided context.")
				.user(u -> u.text(question + "\n\nContext:\n" + contextText))
				.call()
				.content();
	}
}
