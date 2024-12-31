package be.avhconsult.spring_ai_test;

import io.milvus.client.MilvusServiceClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

//    @Bean
//    public CommandLineRunner doChat(Chatter chatter) {
//        return args -> chatter.doStuff();
//    }

    @Bean
    public CommandLineRunner eltRunner(ETL etl) {
        return args -> etl.extractData();
    }

    @Bean
    @Primary
    public ChatModel ollamaModel(OllamaApi api) {
        return OllamaChatModel.builder()
                              .withOllamaApi(api)
                              .withDefaultOptions(OllamaOptions
                                      .create()
                                      .withModel(OllamaModel.LLAMA3.id())
                                      .withKeepAlive("2h")).build();
    }

    @Bean
    @Primary
    public EmbeddingModel embeddingModel(OllamaApi api) {
        return OllamaEmbeddingModel
                .builder()
                .withOllamaApi(api)
                .withDefaultOptions(OllamaOptions.create().withModel(OllamaModel.NOMIC_EMBED_TEXT.id())).build();
    }

    @Bean
    @Primary
    public ChatClient.Builder ollamaClientBuilder(ChatModel model) {
        return ChatClient
                .builder(model)
                .defaultSystem("you are a helpful, polite and friendly chatbot.  people contact you to get help.");
    }

    @Bean
    public DocumentReader tikaReader(@Value("classpath:report.pdf") Resource resource) {
        return new TikaDocumentReader(resource);
    }

    @Bean
    @Primary
    public MilvusVectorStore milvusStore(MilvusServiceClient client, EmbeddingModel embeddingModel) {
        return new MilvusVectorStore(
                (MilvusServiceClient) client.withTimeout(120, TimeUnit.SECONDS),
                embeddingModel,
                true
        );
    }
}
