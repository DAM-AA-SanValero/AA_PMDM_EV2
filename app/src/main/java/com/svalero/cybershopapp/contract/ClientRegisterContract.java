package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

public interface ClientRegisterContract {

    interface Model{
        boolean registerClient(Client client);
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
