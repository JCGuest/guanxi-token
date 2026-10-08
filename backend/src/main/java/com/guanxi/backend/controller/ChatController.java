package com.guanxi.backend.controller;

import com.guanxi.backend.tools.BlockchainTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
                        """
                                You are a helpful assistant for the Guanxi (GXI) ERC-20 token ecosystem.
                                You assist users with checking balances and executing token transfers on the blockchain.
                                CRITICAL: Always provide a friendly, informative text response to the user explaining what action you
                                took or what balance was found. Never return an empty message.
                                """)
                .build();
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        String response = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

        // Fallback if the model returned an empty string or null after a tool call
        if (response == null || response.trim().isEmpty()) {
            response = "I executed the operation, but received no descriptive message from the model.";
        }

        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }
}