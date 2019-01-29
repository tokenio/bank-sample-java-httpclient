package io.token.banksamplehttpclient.client.retrofit;

import io.reactivex.Observable;
import io.token.banksamplehttpclient.client.model.AccountQueryMap;
import io.token.proto.bankapi.Bankapi;
import io.token.proto.bankapi.Bankapi.BatchTransferResponse;
import io.token.proto.bankapi.Bankapi.GetAccountDetailsResponse;
import io.token.proto.bankapi.Bankapi.GetBalanceResponse;
import io.token.proto.bankapi.Bankapi.GetBankAuthorizationResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionResponse;
import io.token.proto.bankapi.Bankapi.GetTransactionsResponse;
import io.token.proto.bankapi.Bankapi.GetValueResponse;
import io.token.proto.bankapi.Bankapi.NotifyResponse;
import io.token.proto.bankapi.Bankapi.RemoveValueResponse;
import io.token.proto.bankapi.Bankapi.ResolveTransferDestinationResponse;
import io.token.proto.bankapi.Bankapi.SetValueResponse;
import io.token.proto.bankapi.Bankapi.TransferResponse;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BankApiService {

    //
    // Account Linking API
    //

    @GET("bank-authorization")
    Observable<GetBankAuthorizationResponse> getBankAuthorization(
            @Query("access_token") String accessToken);

    //
    // Account API
    //

    @GET("accounts/balance")
    Observable<GetBalanceResponse> getBalance(@QueryMap AccountQueryMap account);

    @GET("accounts/transactions")
    Observable<GetTransactionResponse> getTransaction(
            @QueryMap AccountQueryMap account,
            @Query("transaction_id") String transactionId);

    @GET("accounts/transactions")
    Observable<GetTransactionsResponse> getTransactions(
            @QueryMap AccountQueryMap account,
            @Query("offset") String offset,
            @Query("limit") int limit);

    @GET("accounts/transfer-destination")
    Observable<ResolveTransferDestinationResponse> resolveDestination(
            @QueryMap AccountQueryMap account);

    @GET("accounts/account-details")
    Observable<GetAccountDetailsResponse> getAccountDetails(@QueryMap AccountQueryMap account);

    //
    // Notification API
    //

    @POST("notifications")
    Observable<NotifyResponse> notify(@Body Bankapi.NotifyRequest request);

    //
    // Storage API
    //

    @PUT("storage/{key}")
    Observable<SetValueResponse> setValue(
            @Path("key") String key,
            @Body Bankapi.SetValueRequest request);

    @GET("storage/{key}")
    Observable<GetValueResponse> getValue(@Path("key") String key);

    @DELETE("storage/{key}")
    Observable<RemoveValueResponse> removeValue(@Path("key") String key);

    //
    // Transfer API
    //

    @POST("transfers")
    Observable<TransferResponse> transfer(@Body Bankapi.TransferRequest request);

    @POST("batch/transfers")
    Observable<BatchTransferResponse> batchTransfer(@Body Bankapi.BatchTransferRequest request);

    static BankApiService createService(OkHttpClient client, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.addConverterFactory(new JsonToProtoConverterFactory());
        builder.baseUrl(baseUrl);
        builder.client(client);
        Retrofit retrofit = builder.build();

        return retrofit.create(BankApiService.class);
    }
}
