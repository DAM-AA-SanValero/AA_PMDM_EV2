package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientDetailsModel;
import com.svalero.cybershopapp.view.ClientDetailsView;

public class ClientDetailsPresenter implements ClientDetailsContract.Presenter {

    private ClientDetailsModel model;
    private ClientDetailsView view;

    public ClientDetailsPresenter(ClientDetailsView view) {
        this.view = view;
        model = new ClientDetailsModel(view.getApplicationContext());
    }

    @Override
    public void loadClient(String name) {
        Client client = model.loadClient(name);
        view.showClient(client);

    }
}
