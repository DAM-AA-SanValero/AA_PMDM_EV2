package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

public interface ClientRegisterContract {

    interface Model {
        interface OnRegisterClientListener {
            void onRegisterClientSuccess(Client client);

            void onRegisterClientError(String message);
        }

        void registerClient(Client client, OnRegisterClientListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);

        void resetForm();

    }

    interface Presenter {
        void registerClient(Client client);
    }
}
