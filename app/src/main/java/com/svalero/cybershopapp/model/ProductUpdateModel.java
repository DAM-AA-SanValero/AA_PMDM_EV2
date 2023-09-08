package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ProductUpdateContract;
import com.svalero.cybershopapp.domain.Product;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductUpdateModel implements ProductUpdateContract.Model {
    @Override
    public void updateProduct(long id, Product product, OnUpdateProductListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Product> callProduct = cybershopApi.updateProduct(id, product);
            callProduct.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if(response.isSuccessful()){
                    listener.onUpdateProductSuccess(product);
                    } else{
                        String errorMessage = "Error updating product";
                        listener.onUpdateProductError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    t.printStackTrace();
                    String errorMessage = "Error updating product";
                    listener.onUpdateProductError(errorMessage);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
