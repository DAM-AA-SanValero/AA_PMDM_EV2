package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public interface ClientListContract {

    interface Model{
        interface OnLoadClientsListener{
            void onLoadClientsSuccess(List<Client> clients);
            void onLoadClientsError(String message);
        }
        void loadAllClients(OnLoadClientsListener listener);
    }

    interface View {
        void showClients(List<Client> clients);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllClients();
    }

}
