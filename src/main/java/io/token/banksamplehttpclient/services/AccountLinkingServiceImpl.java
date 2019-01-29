package io.token.banksamplehttpclient.services;

import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.proto.bankapi.Bankapi.GetBankAuthorizationRequest;
import io.token.proto.banklink.Banklink.BankAuthorization;
import io.token.sdk.api.service.AccountLinkingService;

public class AccountLinkingServiceImpl implements AccountLinkingService {
    private final BankApiClient client;

    public AccountLinkingServiceImpl(BankApiClient client) {
        this.client = client;
    }

    @Override
    public BankAuthorization getBankAuthorization(String accessToken) {
        return client
                .getBankAuthorization(GetBankAuthorizationRequest.newBuilder()
                        .setAccessToken(accessToken)
                        .build())
                .getBankAuthorization();
    }
}
