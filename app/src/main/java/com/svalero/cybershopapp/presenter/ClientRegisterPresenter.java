package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientRegisterModel;
import com.svalero.cybershopapp.view.ClientRegisterView;

public class ClientRegisterPresenter implements ClientRegisterContract.Presenter {

    private ClientRegisterModel model;
    private ClientRegisterView view;

    public ClientRegisterPresenter(ClientRegisterView view) {
        this.view = view;
        model = new ClientRegisterModel(view.getApplicationContext());
    }

    @Override
    public void registerClient(Client client) {
        boolean registered = model.registerClient(client);
        if(registered){
            view.showMessage("Client registered");
            view.resetForm();
        } else{
            view.showError(String.valueOf(R.string.error_registering));
        }
    }
}
