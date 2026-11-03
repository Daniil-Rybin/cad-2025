package com.example.couriermobile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CourierApi {

    @GET("api/me")
    Call<CurrentUser> getCurrentUser(
            @Header("Authorization") String authorization
    );

    @GET("api/orders")
    Call<List<Order>> getOrders(
            @Header("Authorization") String authorization
    );
}
