package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;

import com.svalero.cybershopapp.contract.RepairDetailsContract;
import com.svalero.cybershopapp.domain.Repair;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairDetailsModel implements RepairDetailsContract.Model {

    @Override
    public void loadRepairById(long repairId, OnLoadRepairListener listener) {
        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Repair> callRepairs = cybershopApi.getRepairById(repairId);
            callRepairs.enqueue(new Callback<Repair>() {
                @Override
                public void onResponse(Call<Repair> call, Response<Repair> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Repair repair = response.body();
                        listener.onLoadRepairSuccess(repair);
                    } else {
                        String message = "API error response";
                        listener.onLoadRepairError(message);
                    }
                }

                @Override
                public void onFailure(Call<Repair> call, Throwable t) {
                    t.printStackTrace();
                    String message = "API error";
                    listener.onLoadRepairError(message);
                }
            });

        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
            String message = "Error en la operación: " + sce.getMessage();
            listener.onLoadRepairError(message);
        }
    }
}
