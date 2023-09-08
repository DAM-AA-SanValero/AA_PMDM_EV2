package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientDetailsModel;
import com.svalero.cybershopapp.view.ClientDetailsView;

public class ClientDetailsPresenter implements ClientDetailsContract.Presenter,
    ClientDetailsContract.Model.OnLoadClientListener{

    private ClientDetailsModel model;
    private ClientDetailsContract.View view;

    public ClientDetailsPresenter(ClientDetailsContract.View view) {
        this.view = view;
        model = new ClientDetailsModel();
    }

    @Override
    public void loadClientById(long clientId) {
        model.loadClientById(clientId, this);
    }

    @Override
    public void getClientDetails(long clientId) {
        loadClientById(clientId);
    }


    @Override
    public void onLoadClientSuccess(Client client) {
        view.showClientDetails(client);
    }

    @Override
    public void onLoadClientError(String message) {
        view.showMessage(message);
    }
}
