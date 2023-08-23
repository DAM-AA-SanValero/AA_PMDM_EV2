package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientRegisterModel;
import com.svalero.cybershopapp.view.ClientRegisterView;

public class ClientRegisterPresenter implements ClientRegisterContract.Presenter,
    ClientRegisterContract.Model.OnRegisterClientListener{

    private ClientRegisterModel model;
    private ClientRegisterView view;

    public ClientRegisterPresenter(ClientRegisterView view) {
        model = new ClientRegisterModel();
        this.view = view;
    }

    @Override
    public void registerClient(Client client) {
        model.registerClient(client, this);
    }

    @Override
    public void onRegisterClientSuccess(Client client) {
        view.showMessage(client.getId() + "se ha registrado correctamente");
        view.resetForm();
    }

    @Override
    public void onRegisterClientError(String message) {
        view.showError(String.valueOf(R.string.error_registering));
    }
}
