package com.safepay.safeai.controllers.request;

import java.util.List;

public record CvRequest(
		String summary,
		List<String> skills,
		List<Experience> experiences,
		List<Project> projects
) {
	public record Experience(
			String company,
			String role,
			String description,
			List<String> technologies
	) {}

	public record Project(
			String name,
			String description,
			List<String> technologies
	) {}
}