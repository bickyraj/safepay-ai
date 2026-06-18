package com.safepay.safeai.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class JobMatchService {

	private final VectorStore vectorStore;
	private final ChatClient chatClient;

	public JobMatchService(VectorStore vectorStore, ChatClient.Builder chatClient) {
		this.vectorStore = vectorStore;
		this.chatClient = chatClient.build();
	}

	public String generateCv(String jobDescription) {

		// 1. Retrieve relevant CV chunks from Chroma
		List<Document> relevantDocs =
				vectorStore.similaritySearch(jobDescription);

		// 2. Build context from retrieved docs
		String context = relevantDocs.stream()
				.map(Document::getFormattedContent)
				.collect(Collectors.joining("\n\n"));

		// 3. Build prompt
		String prompt = buildPrompt(jobDescription, context);

		// 4. Call LLM
		return chatClient
				.prompt()
				.user(prompt)
				.call()
				.content();
	}

	private String buildPrompt(String job, String context) {

		return """
    You are an expert CV generator for senior backend engineers.

    IMPORTANT RULES:
    - Return ONLY valid JSON as a STRING
    - Do NOT include markdown, backticks, or explanations
    - Do NOT wrap output in extra text
    - Output must be directly parsable JSON

    DO NOT INVENT ANY EXPERIENCE OR SKILLS

    OUTPUT FORMAT:

    {
      "professionalSummary": "",
      "keySkills": [],
      "experience": [
        {
          "company": "",
          "role": "",
          "duration": "",
          "highlights": []
        }
      ],
      "projects": [
        {
          "name": "",
          "description": "",
          "technologies": []
        }
      ],
      "technicalSkills": []
    }

    JOB DESCRIPTION:
    %s

    CANDIDATE CONTEXT:
    %s
    """.formatted(job, context);
	}
}