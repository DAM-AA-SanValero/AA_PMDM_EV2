package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.contract.ClientDeleteContract;
import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.model.ClientDeleteModel;
import com.svalero.cybershopapp.model.ClientRegisterModel;
import com.svalero.cybershopapp.view.ClientRegisterView;

public class ClientDeletePresenter implements ClientDeleteContract.Presenter,
    ClientDeleteContract.Model.OnDeleteClientListener{

    private ClientDeleteModel model;
    private ClientAdapter view;

    public ClientDeletePresenter(ClientAdapter view) {
        model = new ClientDeleteModel();
        this.view = view;
    }

    @Override
    public void deleteClient(long clientId) {
        model.deleteClient(clientId, this);
    }

    @Override
    public void onDeleteClientSuccess() {
        view.showMessage("El cliente se ha eliminado correctamente");
    }

    @Override
    public void onDeleteClientError(String message) {
        view.showError("Error al eliminar el cliente");
    }
}
