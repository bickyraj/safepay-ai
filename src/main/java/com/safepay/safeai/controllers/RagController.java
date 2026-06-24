package com.safepay.safeai.controllers;

import java.nio.file.Path;

import com.safepay.safeai.controllers.request.ChatRequestDTO;
import com.safepay.safeai.services.IngestionService;
import com.safepay.safeai.services.RagService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RagController {
	private final RagService ragService;
	private final IngestionService ingestionService;

	@PostMapping("/ingest")
	public void ingest(@RequestParam String path) {
		ingestionService.ingest(Path.of(path));
	}

	@PostMapping("/ask")
	public ResponseEntity<String> ask(@RequestBody ChatRequestDTO chatRequestDTO) {
		return ResponseEntity.ok(ragService
				.ask(chatRequestDTO.getQuestion(), chatRequestDTO.getConversationId()));
	}

	@PostMapping("/ingesturl")
	public void injestUrl(@RequestParam String path) {
		ingestionService.ingestUrl(path);
	}
}
