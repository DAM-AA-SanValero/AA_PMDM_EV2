package com.svalero.cybershopapp.model;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.RepairListContract;
import com.svalero.cybershopapp.domain.Repair;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairListModel implements RepairListContract.Model {

    @Override
    public void loadAllRepairs(OnLoadRepairsListener listener) {
        CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
        Call<List<Repair>> callRepairs = cybershopApi.getRepair();
        callRepairs.enqueue(new Callback<List<Repair>>() {
            @Override
            public void onResponse(Call<List<Repair>> call, Response<List<Repair>> response) {
                List<Repair> repairs = response.body();
                listener.onLoadRepairsSuccess(repairs);
            }

            @Override
            public void onFailure(Call<List<Repair>> call, Throwable t) {
                t.printStackTrace();
                String message = "API error";
                listener.onLoadRepairsError(message);
            }
        });
    }
}
