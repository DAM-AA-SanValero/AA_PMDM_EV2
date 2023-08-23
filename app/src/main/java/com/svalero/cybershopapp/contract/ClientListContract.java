package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public interface ClientListContract {

    interface Model{
        List<Client> loadAllClients();
        List<Client> loadClientsByName(String name);
        boolean deleteClientByName(String name);
    }

    interface View {
        void showClients(List<Client> clients);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllClients();
        void loadClientsByName(String name);
        void deleteClientByName(String name);
    }

}
