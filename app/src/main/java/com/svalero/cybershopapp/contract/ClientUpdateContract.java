package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

public interface ClientUpdateContract {

    interface Model {
        interface OnUpdateClientListener {
            void onUpdateClientSuccess(Client client);

            void onUpdateClientError(String errorMessage);
        }

        void updateClient(long clientId, Client client , OnUpdateClientListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showSuccessMessage(String message);

        void showClientUpdatedMessage(Client client);
    }

    interface Presenter {
        void updateClient(long id, Client client);
    }
}
