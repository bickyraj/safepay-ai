package com.safepay.safeai.controllers;

import com.safepay.safeai.controllers.request.CvRequest;
import com.safepay.safeai.controllers.request.JobRequest;
import com.safepay.safeai.services.CvIngestionService;
import com.safepay.safeai.services.JobMatchService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cv")
@RequiredArgsConstructor
public class CvController {

	private final CvIngestionService cvIngestionService;
	private final JobMatchService jobMatchService;

	@PostMapping("/upload-cv")
	public String uploadCv(@RequestBody CvRequest request) {
		cvIngestionService.ingest(request);
		return "CV ingested successfully";
	}

	@PostMapping("/generate-cv")
	public ResponseEntity<String> generateCv(@RequestBody JobRequest request) {
		return ResponseEntity.ok(jobMatchService.generateCv(request.jobDescription()));
	}
}
