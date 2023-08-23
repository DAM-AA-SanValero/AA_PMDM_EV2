package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.ClientListContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientListModel;
import com.svalero.cybershopapp.view.ClientListView;

import java.util.List;

public class ClientListPresenter implements ClientListContract.Presenter
        ,ClientListContract.Model.OnLoadClientsListener  {

    private ClientListModel model;
    private ClientListView view;

    public ClientListPresenter(ClientListView view) {
        this.view = view;
        this.model = new ClientListModel();

    }

    @Override
    public void loadAllClients() {
        model.loadAllClients(this);
    }

    @Override
    public void loadClientsByName(String name) {

    }

    @Override
    public void deleteClientByName(String name) {

    }

    @Override
    public void onLoadClientsSuccess(List<Client> clients) {
        view.showClients(clients);
    }

    @Override
    public void onLoadClientsError(String message) {
        view.showMessage(message);
    }
}
