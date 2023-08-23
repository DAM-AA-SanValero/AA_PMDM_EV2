package com.svalero.cybershopapp.model;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.content.Context;

import androidx.room.Room;

import com.svalero.cybershopapp.contract.ClientListContract;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public class ClientListModel implements ClientListContract.Model {

    private Context context;
    public ClientListModel(Context context){
        this.context = context;
    }

    @Override
    public List<Client> loadAllClients() {
        final AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();
        return database.clientDao().getAll();

    }

    @Override
    public List<Client> loadClientsByName(String name) {
        return null;
    }

    @Override
    public boolean deleteClientByName(String name) {
        return false;
    }
}
