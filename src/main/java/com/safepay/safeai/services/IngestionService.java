package com.safepay.safeai.services;

import java.nio.file.Path;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionService {
	private final VectorStore vectorStore;

	public void ingest(Path pdfPath) {

		Resource resource = new FileSystemResource(pdfPath);

		PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
				PdfDocumentReaderConfig.builder()
						.withPageTopMargin(0)
						.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
								.withNumberOfTopTextLinesToDelete(0)
								.build())
						.withPagesPerDocument(1)
						.build());

		List<Document> docs = pdfReader.read();

		TokenTextSplitter splitter = TokenTextSplitter.builder()
				.withChunkSize(1000)
				.withMinChunkSizeChars(400)
				.withMinChunkLengthToEmbed(10)
				.withMaxNumChunks(5000)
				.withKeepSeparator(true)
				.build();

		List<Document> chunks = splitter.apply(docs);

		vectorStore.add(chunks);
	}
}
