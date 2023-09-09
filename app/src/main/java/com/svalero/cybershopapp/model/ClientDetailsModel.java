package com.svalero.cybershopapp.model;

import android.database.sqlite.SQLiteConstraintException;
import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.domain.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailsModel implements ClientDetailsContract.Model {

    @Override
    public void loadClientById(long clientId, OnLoadClientListener listener) {
        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Client> callClients = cybershopApi.getClientById(clientId);
            callClients.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Client client = response.body();
                        listener.onLoadClientSuccess(client);
                    } else {
                        String message = "API error response";
                        listener.onLoadClientError(message);
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    t.printStackTrace();
                    String message = "API error";
                    listener.onLoadClientError(message);
                }
            });

        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
            String message = "SQL Error: " + sce.getMessage();
            listener.onLoadClientError(message);
        }
    }
}
