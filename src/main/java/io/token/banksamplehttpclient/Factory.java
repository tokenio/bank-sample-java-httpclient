package io.token.banksamplehttpclient;

import com.typesafe.config.ConfigFactory;
import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.banksamplehttpclient.config.HttpClientConfig;
import io.token.banksamplehttpclient.services.AccountLinkingServiceImpl;
import io.token.banksamplehttpclient.services.AccountServiceImpl;
import io.token.banksamplehttpclient.services.StorageServiceImpl;
import io.token.banksamplehttpclient.services.TransferServiceImpl;
import io.token.sdk.api.service.AccountLinkingService;
import io.token.sdk.api.service.AccountService;
import io.token.sdk.api.service.StorageService;
import io.token.sdk.api.service.TransferService;

import java.io.File;

/**
 * A factory class that is used to instantiate various services that are
 * exposed by the gRPC server.
 */
final class Factory {
    private final BankApiClient bankClient;

    /**
     * Creates new factory instance.
     *
     * @param configFilePath path to the config directory
     */
    Factory(String configFilePath) {
        File configFile = new File(configFilePath);
        HttpClientConfig httpClientConfig = HttpClientConfig.create(ConfigFactory
                .parseFile(configFile)
                .getConfig("http-client")
        );

        this.bankClient = new BankApiClient(httpClientConfig);
    }

    /**
     * Creates new {@link StorageService} instance.
     *
     * @return new storage service instance
     */
    StorageService storageService() {
        return new StorageServiceImpl(bankClient);
    }

    /**
     * Creates new {@link AccountService} instance.
     *
     * @return new account service instance
     */
    AccountService accountService() {
        return new AccountServiceImpl(bankClient);
    }

    /**
     * Creates new {@link AccountLinkingService} instance.
     *
     * @return new account linking service instance
     */
    AccountLinkingService accountLinkingService() {
        return new AccountLinkingServiceImpl(bankClient);
    }

    /**
     * Creates new {@link TransferService} instance.
     *
     * @return new transfer service instance
     */
    TransferService transferService() {
        return new TransferServiceImpl(bankClient);
    }
}
