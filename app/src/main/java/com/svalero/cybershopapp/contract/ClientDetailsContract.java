package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public interface ClientDetailsContract {

    interface Model{

        interface OnLoadClientListener{
            void onLoadClientSuccess(Client clients);
            void onLoadClientError(String message);
        }
        void loadClientById(long clientId, OnLoadClientListener listener);
    }

    interface View {
        void showClientDetails(Client client);
        void showMessage(String message);

    }

    interface Presenter {
        void loadClientById(long id);

    }
}
