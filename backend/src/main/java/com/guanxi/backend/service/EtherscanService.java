package com.guanxi.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EtherscanService {

    private final RestClient restClient;
    private final String apiKey;
    private final String contractAddress;

    public EtherscanService(
            @Value("${etherscan.api-url}") String apiUrl,
            @Value("${etherscan.api-key}") String apiKey,
            @Value("${blockchain.contract-address}") String contractAddress) {
        this.restClient = RestClient.builder().baseUrl(apiUrl).build();
        this.apiKey = apiKey;
        this.contractAddress = contractAddress;
    }

    /**
     * Fetch recent ERC-20 token transfers for a specific wallet or the contract.
     */
    public String getTokenTransferEvents(String walletAddress) {
        try {
            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("module", "account")
                            .queryParam("action", "tokentx")
                            .queryParam("contractaddress", contractAddress)
                            .queryParam("address", walletAddress)
                            .queryParam("page", "1")
                            .queryParam("offset", "10")
                            .queryParam("sort", "desc")
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(Map.class);

            return response != null ? response.toString() : "No transaction data returned.";
        } catch (Exception e) {
            return "Error querying Etherscan: " + e.getMessage();
        }
    }

    /**
     * Query total circulating supply from Etherscan.
     */
    public String getTotalSupply() {
        try {
            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("module", "stats")
                            .queryParam("action", "tokensupply")
                            .queryParam("contractaddress", contractAddress)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(Map.class);

            return response != null ? response.toString() : "No supply data returned.";
        } catch (Exception e) {
            return "Error querying total supply: " + e.getMessage();
        }
    }
}