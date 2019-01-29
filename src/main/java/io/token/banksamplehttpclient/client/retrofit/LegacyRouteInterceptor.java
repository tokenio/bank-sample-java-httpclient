package io.token.banksamplehttpclient.client.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Re-writes routes with {@code Custom} account types in the format existing integrations expect by
 * adding the account to the path instead of the query.
 */
public final class LegacyRouteInterceptor implements Interceptor {
    private static final String BANK_ID_PARAMETER = "account.custom.bank_id";
    private static final String PAYLOAD_PARAMETER = "account.custom.payload";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        if (url.queryParameterNames().contains(BANK_ID_PARAMETER)) {
            String bankId = url.queryParameter(BANK_ID_PARAMETER);
            String payload = url.queryParameter(PAYLOAD_PARAMETER);

            url = url.newBuilder()
                    .removePathSegment(url.pathSize() - 1)
                    .addPathSegment(bankId)
                    .addPathSegment(payload)
                    .addPathSegment(url.pathSegments().get(url.pathSize() - 1))
                    .removeAllQueryParameters(BANK_ID_PARAMETER)
                    .removeAllQueryParameters(PAYLOAD_PARAMETER)
                    .build();

            request = request.newBuilder()
                    .url(url)
                    .build();
        }

        return chain.proceed(request);
    }
}
