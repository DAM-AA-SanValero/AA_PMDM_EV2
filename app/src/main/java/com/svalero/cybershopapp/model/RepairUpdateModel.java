package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.RepairUpdateContract;
import com.svalero.cybershopapp.domain.Repair;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairUpdateModel implements RepairUpdateContract.Model {
    @Override
    public void updateRepair(long id, Repair repair, OnUpdateRepairListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Repair> callRepairs = cybershopApi.updateRepair(id, repair);
            callRepairs.enqueue(new Callback<Repair>() {
                @Override
                public void onResponse(Call<Repair> call, Response<Repair> response) {
                    if(response.isSuccessful()){
                    listener.onUpdateRepairSuccess(repair);
                    } else{
                        String errorMessage = "Error updating repair";
                        listener.onUpdateRepairError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<Repair> call, Throwable t) {
                    t.printStackTrace();
                    String errorMessage = "Error updating repair";
                    listener.onUpdateRepairError(errorMessage);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
