package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.RepairRegisterContract;
import com.svalero.cybershopapp.domain.Repair;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairRegisterModel implements RepairRegisterContract.Model {
    @Override
    public void registerRepair(Repair repair, OnRegisterRepairListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Repair> callRepairs = cybershopApi.addRepair(repair);
            callRepairs.enqueue(new Callback<Repair>() {
                @Override
                public void onResponse(Call<Repair> call, Response<Repair> response) {
                    if(response.isSuccessful()){
                        listener.onRegisterRepairSuccess(repair);
                    } else{
                        String errorMessage = "Error registering repair";
                        listener.onRegisterRepairError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<Repair> call, Throwable t) {
                    t.printStackTrace();
                    String message = "API error";
                    listener.onRegisterRepairError(message);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
