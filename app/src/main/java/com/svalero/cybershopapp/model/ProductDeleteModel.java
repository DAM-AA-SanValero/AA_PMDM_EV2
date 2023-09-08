package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ProductDeleteContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDeleteModel implements ProductDeleteContract.Model {
    @Override
    public void deleteProduct(long productId, OnDeleteProductListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Void> callProducts = cybershopApi.deleteProduct(productId);
            callProducts.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    listener.onDeleteProductSuccess();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onDeleteProductError(message);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
