package com.svalero.cybershopapp.api;

import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.domain.Repair;

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
    @GET("products")
    Call<List<Product>> getProduct();
    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") long id);
    @GET("repairs")
    Call<List<Repair>> getRepair();
    @GET("repairs/{id}")
    Call<Repair> getRepairById(@Path("id") long id);

    @POST("clients")
    Call<Client> addClient(@Body Client client);
    @POST("products")
    Call<Product> addProduct(@Body Product product);
    @POST("repairs")
    Call<Repair> addRepair(@Body Repair repair);
    @DELETE("clients/{id}")
    Call<Void> deleteClient(@Path("id") long id);
    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") long id);
    @DELETE("repairs/{id}")
    Call<Void> deleteRepair(@Path("id") long id);

    @PUT("clients/{id}")
    Call<Client> updateClient(@Path("id") long id, @Body Client client);
    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") long id, @Body Product product);
    @PUT("repairs/{id}")
    Call<Repair> updateRepair(@Path("id") long id, @Body Repair repair);


}
