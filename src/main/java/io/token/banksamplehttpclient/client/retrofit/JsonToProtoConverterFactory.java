package io.token.banksamplehttpclient.client.retrofit;

import com.google.protobuf.Message;
import io.token.proto.ProtoJson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Used to convert JSON HTTP responses to protobuf objects.
 */
public class JsonToProtoConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> klass = (Class<?>) type;
        if (!Message.class.isAssignableFrom(klass)) {
            return null;
        }
        return new ProtoToJsonConverter();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> klass = (Class<?>) type;
        if (!Message.class.isAssignableFrom(klass)) {
            return null;
        }
        return new JsonToProtoConverter(klass);
    }

    final class ProtoToJsonConverter implements Converter<Message, RequestBody> {
        private final MediaType mediaType = MediaType.get("application/x-protobuf");

        @Override
        public RequestBody convert(Message value) throws IOException {
            return RequestBody.create(mediaType, ProtoJson.toJson(value).getBytes());
        }
    }

    final class JsonToProtoConverter implements Converter<ResponseBody, Message> {
        private final Class<?> klass;

        JsonToProtoConverter(Class<?> klass) {
            this.klass = klass;
        }

        @Override
        public Message convert(ResponseBody value) throws IOException {
            try {
                Message.Builder builder = (Message.Builder) klass
                        .getDeclaredMethod("newBuilder")
                        .invoke(null);
                return ProtoJson.fromJson(value.string(), builder);
            } catch (IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
