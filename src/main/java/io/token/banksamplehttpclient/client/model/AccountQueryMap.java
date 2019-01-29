package io.token.banksamplehttpclient.client.model;

import com.google.common.collect.ImmutableMap;
import io.token.proto.common.account.AccountProtos.BankAccount;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Converts a {@link BankAccount} into a {@link Map} to be converted to query parameters. Must be
 * annotated with {@code @QueryMap} in retrofit service definition.
 */
public final class AccountQueryMap implements Map<String, String> {
    private final ImmutableMap<String, String> delegate;

    public AccountQueryMap(BankAccount account) {
        delegate = toQueryMap(account);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return delegate.get(key);
    }

    @Override
    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<String> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return delegate.entrySet();
    }

    private static ImmutableMap<String, String> toQueryMap(BankAccount account) {
        ImmutableMap.Builder<String, String> query = ImmutableMap.builder();
        switch (account.getAccountCase()) {
            case SWIFT:
                query.put("account.swift.bic", account.getSwift().getBic());
                query.put("account.swift.account", account.getSwift().getAccount());
                break;
            case SEPA:
                query.put("account.sepa.iban", account.getSepa().getIban());
                query.put("account.sepa.bic", account.getSepa().getBic());
                break;
            case ACH:
                query.put("account.ach.routing", account.getAch().getRouting());
                query.put("account.ach.account", account.getAch().getAccount());
                break;
            case FASTER_PAYMENTS:
                query.put(
                        "account.faster_payments.sort_code",
                        account.getFasterPayments().getSortCode());
                query.put(
                        "account.faster_payments.account_number",
                        account.getFasterPayments().getAccountNumber());
                break;
            case CUSTOM:
                query.put("account.custom.bank_id", account.getCustom().getBankId());
                query.put("account.custom.payload", account.getCustom().getPayload());
                break;
            case BANK:
            case TOKEN:
            case ACCOUNT_NOT_SET:
            default:
                throw new IllegalArgumentException(
                        "Invalid account type: " + account.getAccountCase());
        }
        return query.build();
    }
}
