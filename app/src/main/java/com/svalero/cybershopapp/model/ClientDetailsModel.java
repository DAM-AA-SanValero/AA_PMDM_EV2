package com.svalero.cybershopapp.model;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.database.AppDatabase;
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
                        String message = "Error en la operación: Respuesta inválida de la API";
                        listener.onLoadClientError(message);
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onLoadClientError(message);
                }
            });

        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
            String message = "Error en la operación: " + sce.getMessage();
            listener.onLoadClientError(message);
        }
    }
}
