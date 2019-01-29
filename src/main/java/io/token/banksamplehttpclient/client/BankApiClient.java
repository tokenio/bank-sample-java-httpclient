package io.token.banksamplehttpclient.client;

import io.token.banksamplehttpclient.client.model.AccountQueryMap;
import io.token.banksamplehttpclient.client.retrofit.BankApiService;
import io.token.banksamplehttpclient.client.retrofit.ExceptionInterceptor;
import io.token.banksamplehttpclient.client.retrofit.LegacyRouteInterceptor;
import io.token.banksamplehttpclient.config.HttpClientConfig;
import io.token.proto.bankapi.Bankapi.BatchTransferRequest;
import io.token.proto.bankapi.Bankapi.BatchTransferResponse;
import io.token.proto.bankapi.Bankapi.GetAccountDetailsRequest;
import io.token.proto.bankapi.Bankapi.GetAccountDetailsResponse;
import io.token.proto.bankapi.Bankapi.GetBalanceRequest;
import io.token.proto.bankapi.Bankapi.GetBalanceResponse;
import io.token.proto.bankapi.Bankapi.GetBankAuthorizationRequest;
import io.token.proto.bankapi.Bankapi.GetBankAuthorizationResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionRequest;
import io.token.proto.bankapi.Bankapi.GetTransactionResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionsRequest;
import io.token.proto.bankapi.Bankapi.GetTransactionsResponse;
import io.token.proto.bankapi.Bankapi.GetValueRequest;
import io.token.proto.bankapi.Bankapi.GetValueResponse;
import io.token.proto.bankapi.Bankapi.NotifyRequest;
import io.token.proto.bankapi.Bankapi.NotifyResponse;
import io.token.proto.bankapi.Bankapi.RemoveValueRequest;
import io.token.proto.bankapi.Bankapi.RemoveValueResponse;
import io.token.proto.bankapi.Bankapi.ResolveTransferDestinationRequest;
import io.token.proto.bankapi.Bankapi.ResolveTransferDestinationResponse;
import io.token.proto.bankapi.Bankapi.SetValueRequest;
import io.token.proto.bankapi.Bankapi.SetValueResponse;
import io.token.proto.bankapi.Bankapi.TransferRequest;
import io.token.proto.bankapi.Bankapi.TransferResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public final class BankApiClient {
    private final BankApiService service;

    public BankApiClient(HttpClientConfig clientConfig) {
        this.service = BankApiService.createService(
                createHttpClient(clientConfig),
                clientConfig.getHostUrl());
    }

    /**
     * Gets a bank authorization payload given OAuth access token.
     *
     * @param request get authorization request
     * @return get authorization response
     */
    public GetBankAuthorizationResponse getBankAuthorization(GetBankAuthorizationRequest request) {
        return service
                .getBankAuthorization(request.getAccessToken())
                .blockingSingle();
    }

    /**
     * Fetches balances given bank account.
     *
     * @param request get balance request
     * @return get balance response
     */
    public GetBalanceResponse getBalance(GetBalanceRequest request) {
        return service
                .getBalance(new AccountQueryMap(request.getAccount()))
                .blockingSingle();
    }

    /**
     * Fetches a single transaction given bank account and the transaction ID.
     *
     * @param request get transaction request
     * @return get transaction response
     */
    public GetTransactionResponse getTransaction(GetTransactionRequest request) {
        return service
                .getTransaction(
                        new AccountQueryMap(request.getAccount()),
                        request.getTransactionId())
                .blockingSingle();
    }

    /**
     * Fetches transactions given bank account, offset and limit.
     *
     * @param request get transactions request
     * @return get transactions response
     */
    public GetTransactionsResponse getTransactions(GetTransactionsRequest request) {
        return service
                .getTransactions(
                        new AccountQueryMap(request.getAccount()),
                        request.getOffset(),
                        request.getLimit())
                .blockingSingle();
    }

    /**
     * Fetches resolved transfer destinations for the given account.
     *
     * @param request resolve destination request
     * @return resolve destinations response
     */
    public ResolveTransferDestinationResponse resolveDestination(
            ResolveTransferDestinationRequest request) {
        return service
                .resolveDestination(new AccountQueryMap(request.getAccount()))
                .blockingSingle();
    }

    /**
     * Fetches account details for the given bank account.
     *
     * @param request get account details request
     * @return get account details response
     */
    public GetAccountDetailsResponse getAccountDetails(GetAccountDetailsRequest request) {
        return service
                .getAccountDetails(new AccountQueryMap(request.getAccount()))
                .blockingSingle();
    }

    /**
     * Sends a notification.
     *
     * @param request notify request
     * @return notify response
     */
    public NotifyResponse notify(NotifyRequest request) {
        return service
                .notify(request)
                .blockingSingle();
    }

    /**
     * Sets a value in the storage service.
     *
     * @param request set value request
     * @return set value response
     */
    public SetValueResponse setValue(SetValueRequest request) {
        return service
                .setValue(request.getKey(), request)
                .blockingSingle();
    }

    /**
     * Retrieves a value in the storage service.
     *
     * @param request get value request
     * @return get value response
     */
    public GetValueResponse getValue(GetValueRequest request) {
        return service
                .getValue(request.getKey())
                .blockingSingle();
    }

    /**
     * Removes a value in the storage service.
     *
     * @param request remove value request
     * @return remove value response
     */
    public RemoveValueResponse removeValue(RemoveValueRequest request) {
        return service
                .removeValue(request.getKey())
                .blockingSingle();
    }

    /**
     * Creates a transfer.
     *
     * @param request transfer request
     * @return transfer response
     */
    public TransferResponse transfer(TransferRequest request) {
        return service
                .transfer(request)
                .blockingSingle();
    }

    /**
     * Creates a batch of transfers.
     *
     * @param request batch transfer request
     * @return batch transfer response
     */
    public BatchTransferResponse batchTransfer(BatchTransferRequest request) {
        return service
                .batchTransfer(request)
                .blockingSingle();
    }

    private static OkHttpClient createHttpClient(HttpClientConfig config) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(config.getOkHttpLogLevel());

        OkHttpClient client = new OkHttpClient.Builder()
                // Should preceded logging so that final requests are logged
                .addInterceptor(new LegacyRouteInterceptor())
                // Should preceded logging so response is logged before throwing
                .addInterceptor(new ExceptionInterceptor())
                .addInterceptor(loggingInterceptor)
                // Should follow logging so that header is not logged (unless log level is BASIC)
                .readTimeout(config.getOkHttpReadTimeout().getSeconds(), TimeUnit.SECONDS)
                .build();

        return client;
    }
}
