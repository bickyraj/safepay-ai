package com.safepay.safeai.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

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

	public void ingestUrl(String url) {

		// EXTRACT
		org.jsoup.nodes.Document html = null;
		try {
			html = Jsoup.connect(url).get();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		String text = html.body().text();

		Document doc = new Document(text);
		doc.getMetadata().put("source", url);

		// TRANSFORM
		TokenTextSplitter splitter = new TokenTextSplitter();
		List<Document> chunks = splitter.split(doc);

		// LOAD
		vectorStore.add(chunks);
	}
}
