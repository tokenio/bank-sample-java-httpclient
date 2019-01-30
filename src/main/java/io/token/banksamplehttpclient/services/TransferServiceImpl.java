package io.token.banksamplehttpclient.services;

import static io.token.proto.MoneyUtil.newMoney;
import static io.token.proto.bankapi.Bankapi.StatusCode.SUCCESS;

import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.proto.bankapi.Bankapi.TransferRequest;
import io.token.proto.bankapi.Bankapi.TransferResponse;
import io.token.sdk.api.Transfer;
import io.token.sdk.api.TransferException;
import io.token.sdk.api.service.TransferService;

/**
 * Sample implementation of the {@link TransferService}. Returns fake data.
 */
public class TransferServiceImpl implements TransferService {
    private final BankApiClient client;

    public TransferServiceImpl(BankApiClient client) {
        this.client = client;
    }

    @Override
    public String transfer(Transfer transfer) throws TransferException {
        TransferResponse response = client
                .transfer(TransferRequest.newBuilder()
                        .setTokenRefId(transfer.getTokenRefId())
                        .setTransferRefId(transfer.getTransferRefId())
                        .setTransferId(transfer.getTokenTransferId())
                        .setRequestedAmount(newMoney(
                                transfer.getRequestedAmount(),
                                transfer.getRequestedAmountCurrency()))
                        .setTransactionAmount(newMoney(
                                transfer.getTransactionAmount(),
                                transfer.getTransactionAmountCurrency()))
                        .setSource(transfer.getAccount())
                        .addAllDestinations(transfer.getDestinations())
                        .setDescription(transfer.getDescription())
                        .setMetadata(transfer.getMetadata())
                        .build()
                );

        if (response.getStatus() != SUCCESS) {
            throw new TransferException(
                    response.getStatus(),
                    "Transfer failure with status code: " + response.getStatus().name());
        } else {
            return response.getTransactionId();
        }
    }
}
