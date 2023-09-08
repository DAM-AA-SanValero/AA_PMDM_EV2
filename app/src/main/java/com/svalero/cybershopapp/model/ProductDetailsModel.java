package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ProductDetailsContract;
import com.svalero.cybershopapp.domain.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsModel implements ProductDetailsContract.Model {

    @Override
    public void loadProductById(long productId, OnLoadProductListener listener) {
        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Product> callProducts = cybershopApi.getProductById(productId);
            callProducts.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Product product = response.body();
                        listener.onLoadProductSuccess(product);
                    } else {
                        String message = "Error en la operación: Respuesta inválida de la API";
                        listener.onLoadProductError(message);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onLoadProductError(message);
                }
            });

        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
            String message = "Error en la operación: " + sce.getMessage();
            listener.onLoadProductError(message);
        }
    }
}
