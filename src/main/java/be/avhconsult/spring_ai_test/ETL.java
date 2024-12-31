package be.avhconsult.spring_ai_test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ETL {
    private final DocumentReader reader;
    private final ChatModel model;
    private final VectorStore store;

    public void extractData() {
        log.info("read files");
        var docs = reader.read();
        log.info("found {} docs", docs.size());
//        log.info("enriching metadata");
//        var enriched = new KeywordMetadataEnricher(model, 5).apply(docs);
//        log.info("found {} enriched docs", enriched.size());
//        log.info("metadata {}", enriched.getFirst().getMetadata());
        var splitter = new TokenTextSplitter();
        var splitted = splitter.split(docs);
        log.info("found {} splitted docs", splitted.size());
        log.info("adding to vector store");
        store.add(splitted);
        var similarResults = store.similaritySearch(SearchRequest.query("durable"));
        log.info("similarity results {}", similarResults.size());
        log.info("done");
    }
}
