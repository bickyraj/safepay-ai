package com.safepay.safeai.controllers;

import java.nio.file.Path;

import com.safepay.safeai.services.IngestionService;
import com.safepay.safeai.services.RagService;
import lombok.RequiredArgsConstructor;

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
	public String ask(@RequestBody String question) {
		return ragService.ask(question);
	}
}
