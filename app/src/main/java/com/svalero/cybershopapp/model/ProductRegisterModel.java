package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.contract.ProductRegisterContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRegisterModel implements ProductRegisterContract.Model {
    @Override
    public void registerProduct(Product product, OnRegisterProductListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Product> callProducts = cybershopApi.addProduct(product);
            callProducts.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                    listener.onRegisterProductSuccess(product);
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onRegisterProductError(message);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
