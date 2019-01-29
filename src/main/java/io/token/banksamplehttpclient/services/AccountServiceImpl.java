package io.token.banksamplehttpclient.services;

import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.proto.PagedList;
import io.token.proto.bankapi.Bankapi.GetBalanceRequest;
import io.token.proto.bankapi.Bankapi.GetBalanceResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionRequest;
import io.token.proto.bankapi.Bankapi.GetTransactionResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionsRequest;
import io.token.proto.bankapi.Bankapi.GetTransactionsResponse;
import io.token.proto.bankapi.Bankapi.ResolveTransferDestinationRequest;
import io.token.proto.common.account.AccountProtos.BankAccount;
import io.token.proto.common.transaction.TransactionProtos.Transaction;
import io.token.proto.common.transferinstructions.TransferInstructionsProtos.TransferEndpoint;
import io.token.sdk.api.Balance;
import io.token.sdk.api.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    private final BankApiClient client;

    public AccountServiceImpl(BankApiClient client) {
        this.client = client;
    }

    @Override
    public Balance getBalance(BankAccount account) {
        return toBalance(client
                .getBalance(GetBalanceRequest.newBuilder()
                        .setAccount(account)
                        .build())
        );
    }

    @Override
    public Optional<Transaction> getTransaction(BankAccount account, String transactionId) {
        return toOptionalTransaction(client
                .getTransaction(GetTransactionRequest.newBuilder()
                        .setAccount(account)
                        .setTransactionId(transactionId)
                        .build())
        );
    }

    @Override
    public PagedList<Transaction, String> getTransactions(
            BankAccount account,
            String cursor,
            int limit) {
        return toTransactions(client
                .getTransactions(GetTransactionsRequest.newBuilder()
                        .setAccount(account)
                        .setOffset(cursor)
                        .setLimit(limit)
                        .build())
        );
    }

    @Override
    public List<TransferEndpoint> resolveTransferDestination(BankAccount bankAccount) {
        return client
                .resolveDestination(ResolveTransferDestinationRequest.newBuilder()
                        .setAccount(bankAccount)
                        .build())
                .getEndpointsList();
    }

    private static Balance toBalance(GetBalanceResponse response) {
        return Balance.create(
                response.getCurrent().getCurrency(), // TODO confirm
                new BigDecimal(response.getAvailable().getValue()),
                new BigDecimal(response.getCurrent().getValue()),
                response.getUpdatedAtMs(),
                response.getOtherBalancesList()
        );
    }

    private static Optional<Transaction> toOptionalTransaction(GetTransactionResponse response) {
        return response.hasTransaction()
                ? Optional.of(response.getTransaction())
                : Optional.empty();
    }

    private static PagedList<Transaction, String> toTransactions(GetTransactionsResponse response) {
        return PagedList.create(
                response.getTransactionsList(),
                response.getOffset());
    }
}
