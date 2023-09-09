package com.svalero.cybershopapp.presenter;

import android.content.Context;

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

    Context context;
    private ClientDeleteModel model;
    private ClientAdapter view;

    public ClientDeletePresenter(ClientAdapter view) {
        model = new ClientDeleteModel();
        this.view = view;
        this.context = view.context;
    }

    @Override
    public void deleteClient(long clientId) {
        model.deleteClient(clientId, this);
    }

    @Override
    public void onDeleteClientSuccess() {
        view.showMessage(context.getString(R.string.clientDeleted));
    }

    @Override
    public void onDeleteClientError(String message) {
        view.showError(context.getString(R.string.client_not_deleted));
    }
}
