package com.svalero.cybershopapp.model;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ProductListContract;
import com.svalero.cybershopapp.domain.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListModel implements ProductListContract.Model {

    @Override
    public void loadAllProducts(OnLoadProductsListener listener) {
        CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
        Call<List<Product>> callProducts = cybershopApi.getProduct();
        callProducts.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
                listener.onLoadProductsSuccess(products);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
                String message = "Error en la operación";
                listener.onLoadProductsError(message);
            }
        });
    }
}
