package com.svalero.cybershopapp.model;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRegisterModel implements ClientRegisterContract.Model {
    @Override
    public void registerClient(Client client, OnRegisterClientListener listener) {

        try {
            CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
            Call<Client> callClients = cybershopApi.addClient(client);
            callClients.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    Client client = response.body();
                    listener.onRegisterClientSuccess(client);
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    t.printStackTrace();
                    String message = "Error en la operación";
                    listener.onRegisterClientError(message);
                }
            });

        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
        }

    }
}
