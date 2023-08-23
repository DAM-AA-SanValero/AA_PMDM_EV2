package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Client;

public interface ClientDetailsContract {

    interface Model{
        Client loadClient(String name);
    }

    interface View {
        void showClient(Client client);

    }

    interface Presenter {
        void loadClient(String name);

    }
}
