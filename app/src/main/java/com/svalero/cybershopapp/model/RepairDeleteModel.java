package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.RepairDeleteContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairDeleteModel implements RepairDeleteContract.Model {
    @Override
    public void deleteRepair(long repairId, OnDeleteRepairListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Void> callRepairs = cybershopApi.deleteRepair(repairId);
            callRepairs.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    listener.onDeleteRepairSuccess();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onDeleteRepairError(message);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
