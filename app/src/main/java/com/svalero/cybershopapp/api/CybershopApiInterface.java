package com.svalero.cybershopapp.api;

import com.svalero.cybershopapp.domain.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CybershopApiInterface {
    @GET("clients")
    Call<List<Client>> getClient();
    @GET("clients/{id}")
    Call<Client> getClientById(@Path("id") long id);

    @POST("clients")
    Call<Client> addClient(@Body Client client);


    @DELETE("clients/{id}")
    Call<Void> deleteClient(@Path("id") long id);

    @PUT("clients/{id}")
    Call<Client> updateClient(@Path("id") long id, @Body Client client);


}
