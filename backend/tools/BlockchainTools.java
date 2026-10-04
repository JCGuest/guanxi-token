package com.guanxi.backend.tools;

import com.guanxi.backend.service.BlockchainService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BlockchainTools {

    private final BlockchainService blockchainService;

    public BlockchainTools(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @Tool(description = "Check the Guanxi (GXI) token balance for a specified Ethereum wallet address.")
    public String checkTokenBalance(String walletAddress) {
        try {
            BigDecimal balance = blockchainService.getBalance(walletAddress);
            return "The wallet " + walletAddress + " has " + balance + " GXI tokens.";
        } catch (Exception e) {
            return "Error checking balance: " + e.getMessage();
        }
    }

    @Tool(description = "Transfer Guanxi (GXI) tokens to a recipient Ethereum address.")
    public String transferTokens(String recipientAddress, String amount) {
        try {
            BigDecimal transferAmount = new BigDecimal(amount);
            String txHash = blockchainService.transferTokens(recipientAddress, transferAmount);
            return "Successfully sent " + amount + " GXI to " + recipientAddress + ". Transaction Hash: " + txHash;
        } catch (Exception e) {
            return "Transfer failed: " + e.getMessage();
        }
    }
}