package io.token.banksamplehttpclient.services;

import com.google.protobuf.ByteString;
import io.token.banksamplehttpclient.client.BankApiClient;
import io.token.proto.bankapi.Bankapi.GetValueRequest;
import io.token.proto.bankapi.Bankapi.RemoveValueRequest;
import io.token.proto.bankapi.Bankapi.SetValueRequest;
import io.token.proto.bankapi.Bankapi.SetValueRequest.ContentCategory;
import io.token.sdk.api.service.StorageService;

import java.util.Optional;

public class StorageServiceImpl implements StorageService {
    private final BankApiClient client;

    public StorageServiceImpl(BankApiClient client) {
        this.client = client;
    }

    @Override
    public Optional<byte[]> getValue(String key) {
        ByteString value = client
                .getValue(GetValueRequest.newBuilder()
                        .setKey(key)
                        .build())
                .getValue();

        return value.isEmpty() ? Optional.empty() : Optional.of(value.toByteArray());
    }

    @Override
    public Optional<byte[]> setValue(
            String key,
            ContentCategory category,
            byte[] value) {
        ByteString previous = client
                .setValue(SetValueRequest.newBuilder()
                        .setKey(key)
                        .setValue(ByteString.copyFrom(value))
                        .setCategory(category)
                        .build())
                .getPrevious();

        return previous.isEmpty() ? Optional.empty() : Optional.of(previous.toByteArray());
    }

    @Override
    public void removeValue(String key) {
        client.removeValue(RemoveValueRequest.newBuilder()
                .setKey(key)
                .build());
    }
}
