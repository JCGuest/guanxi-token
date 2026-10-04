package com.guanxi.backend.controller;

import com.guanxi.backend.tools.BlockchainTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, BlockchainTools tools) {
        this.chatClient = builder
                .defaultTools(tools)
                .defaultSystem(
                        "You are a friendly assistant for the Guanxi (GXI) ERC-20 token ecosystem. Help users check balances and transfer tokens on the blockchain.")
                .build();
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
        return Map.of("response", response);
    }
}