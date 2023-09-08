package com.svalero.cybershopapp.contract;
public interface ClientDeleteContract {

    interface Model {
        interface OnDeleteClientListener {
            void onDeleteClientSuccess();

            void onDeleteClientError(String message);
        }

        void deleteClient(long clientId, OnDeleteClientListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);
    }

    interface Presenter {
        void deleteClient(long clientId);
    }
}
