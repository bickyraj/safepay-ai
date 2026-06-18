package com.safepay.safeai.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.safepay.safeai.controllers.request.CvRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CvIngestionService {

	private final VectorStore vectorStore;

	public void ingest(CvRequest request) {

		List<Document> docs = new ArrayList<>();

		// SKILLS
		docs.add(Document.builder()
				.text(request.skills().toString())
				.metadata(Map.of("type", "skills"))
						.build());
		// EXPERIENCES
		for (CvRequest.Experience exp : request.experiences()) {

			String text = """
                Company: %s
                Role: %s
                Description: %s
                Technologies: %s
            """.formatted(
					exp.company(),
					exp.role(),
					exp.description(),
					exp.technologies()
			);

			docs.add(Document.builder()
					.text(text)
					.metadata(Map.of(
							"type", "experience",
							"company", exp.company()
					))
					.build());
		}

		// PROJECTS
		for (CvRequest.Project p : request.projects()) {

			String text = """
                Project: %s
                Description: %s
                Technologies: %s
            """.formatted(
					p.name(),
					p.description(),
					p.technologies()
			);

			docs.add(Document.builder()
					.text(text)
					.metadata(Map.of(
							"type", "project",
							"project", p.name()
					)).build());
		}

		vectorStore.add(docs);
	}
}
