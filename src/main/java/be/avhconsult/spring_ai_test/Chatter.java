package be.avhconsult.spring_ai_test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Chatter {
    private final ChatClient.Builder builder;

    public void doStuff() {
        log.info("chatting with LLM");
        var content = builder.build().prompt().user("explain how spring ai works in max 100 words").call().content();
        log.info("LLM response : {}", content);
    }
}
