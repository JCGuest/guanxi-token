package com.guanxi.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final String contractAddress;

    public BlockchainService(
            @Value("${blockchain.rpc-url}") String rpcUrl,
            @Value("${blockchain.wallet-private-key}") String privateKey,
            @Value("${blockchain.contract-address}") String contractAddress) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
        this.contractAddress = contractAddress;
    }

    public BigDecimal getBalance(String walletAddress) throws Exception {
        ERC20 contract = ERC20.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        BigInteger rawBalance = contract.balanceOf(walletAddress).send();
        BigInteger decimals = contract.decimals().send();
        return new BigDecimal(rawBalance).divide(BigDecimal.TEN.pow(decimals.intValue()));
    }

    public String transferTokens(String toAddress, BigDecimal amount) throws Exception {
        ERC20 contract = ERC20.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        BigInteger decimals = contract.decimals().send();
        BigInteger rawAmount = amount.multiply(BigDecimal.TEN.pow(decimals.intValue())).toBigInteger();
        var receipt = contract.transfer(toAddress, rawAmount).send();
        return receipt.getTransactionHash();
    }
}