package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.domain.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientUpdateModel implements ClientUpdateContract.Model {
    @Override
    public void updateClient(long id, Client client, OnUpdateClientListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Client> callClients = cybershopApi.updateClient(id, client);
            callClients.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if(response.isSuccessful()){
                    listener.onUpdateClientSuccess(client);
                    } else{
                        String errorMessage = "Error updating client";
                        listener.onUpdateClientError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    t.printStackTrace();
                    String errorMessage = "Error updating client";
                    listener.onUpdateClientError(errorMessage);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
