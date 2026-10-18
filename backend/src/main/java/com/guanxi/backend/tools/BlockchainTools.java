package com.guanxi.backend.tools;

import com.guanxi.backend.service.BlockchainService;
import com.guanxi.backend.service.EtherscanService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BlockchainTools {

    private final BlockchainService blockchainService;
    private final EtherscanService etherscanService;

    public BlockchainTools(BlockchainService blockchainService, EtherscanService etherscanService) {
        this.blockchainService = blockchainService;
        this.etherscanService = etherscanService;
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

    @Tool(description = "Query Etherscan for recent Guanxi (GXI) token transfer transactions for a given wallet address.")
    public String getRecentTransactions(String walletAddress) {
        return etherscanService.getTokenTransferEvents(walletAddress);
    }

    @Tool(description = "Query Etherscan for the total on-chain supply of Guanxi (GXI) tokens.")
    public String getTotalSupply() {
        return etherscanService.getTotalSupply();
    }
}