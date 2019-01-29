package io.token.banksamplehttpclient.config;

import com.google.auto.value.AutoValue;
import com.typesafe.config.Config;

import java.time.Duration;

import okhttp3.logging.HttpLoggingInterceptor;

@AutoValue
public abstract class HttpClientConfig {
    /**
     * Constructs a new HttpClientConfig from a given config object.
     *
     * @param config the configuration object
     * @return HttpClientConfig instance
     */
    public static HttpClientConfig create(Config config) {
        return create(
                config.getEnum(HttpLoggingInterceptor.Level.class, "ok-http-log-level"),
                config.getDuration("ok-http-read-timeout"),
                config.getString("host-url"));
    }

    public static HttpClientConfig create(
            HttpLoggingInterceptor.Level logLevel,
            Duration readTimeout,
            String hostUrl) {
        return new AutoValue_HttpClientConfig(logLevel, readTimeout, hostUrl);
    }

    public abstract HttpLoggingInterceptor.Level getOkHttpLogLevel();

    public abstract Duration getOkHttpReadTimeout();

    public abstract String getHostUrl();
}
