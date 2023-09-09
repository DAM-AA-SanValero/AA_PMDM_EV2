package com.svalero.cybershopapp.model;

import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientListContract;
import com.svalero.cybershopapp.domain.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientListModel implements ClientListContract.Model {

    @Override
    public void loadAllClients(OnLoadClientsListener listener) {
        CybershopApiInterface cybershopApi = CybershopApi.buildInstance();
        Call<List<Client>> callClients = cybershopApi.getClient();
        callClients.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                List<Client> clients = response.body();
                listener.onLoadClientsSuccess(clients);
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                t.printStackTrace();
                String message = "API reading error";
                listener.onLoadClientsError(message);
            }
        });
    }
}
