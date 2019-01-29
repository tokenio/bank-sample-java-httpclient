package io.token.banksamplehttpclient.client.retrofit;

import io.grpc.Status;

import java.io.IOException;
import java.util.Optional;

import okhttp3.Interceptor;
import okhttp3.Response;

public final class ExceptionInterceptor implements Interceptor {
    private static final String STATUS_CODE = "token-grpc-status";
    private static final String STATUS_MESSAGE = "token-grpc-message";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Optional.ofNullable(response.header(STATUS_CODE))
                .map(code -> Status.fromCodeValue(Integer.valueOf(code)))
                .ifPresent(code -> {
                    if (!code.isOk()) {
                        throw code
                                .withDescription(response.header(STATUS_MESSAGE))
                                .asRuntimeException();
                    }
                });
        return response;
    }
}
