package io.token.banksamplehttpclient.services;

import static io.token.proto.MoneyUtil.newMoney;

import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.proto.bankapi.Bankapi.TransferRequest;
import io.token.proto.common.money.MoneyProtos.Money;
import io.token.sdk.api.Transfer;
import io.token.sdk.api.TransferException;
import io.token.sdk.api.service.TransferService;

import java.math.BigDecimal;

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
        return client
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
                        .setQuote(transfer.getQuote())
                        .setFeeResponsibility(transfer.getFeeResponsibility())
                        .setDescription(transfer.getDescription())
                        .setMetadata(transfer.getMetadata())
                        .build())
                .getTransactionId();
    }
}
