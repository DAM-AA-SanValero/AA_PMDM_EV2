package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientRegisterModel;
import com.svalero.cybershopapp.model.ClientUpdateModel;
import com.svalero.cybershopapp.view.ClientRegisterView;
import com.svalero.cybershopapp.view.ClientUpdateView;

public class ClientUpdatePresenter implements ClientUpdateContract.Presenter,
    ClientUpdateContract.Model.OnUpdateClientListener{

    private ClientUpdateModel model;
    private ClientUpdateContract.View view;

    public ClientUpdatePresenter(ClientUpdateContract.View view) {
        model = new ClientUpdateModel();
        this.view = view;
    }
    @Override
    public void updateClient(long id, Client client) {
        model.updateClient(id, client, this);
    }

    @Override
    public void onUpdateClientSuccess(Client client) {
        view.showClientUpdatedMessage(client);
    }

    @Override
    public void onUpdateClientError(String errorMessage) {
        view.showError(errorMessage);
    }
}

