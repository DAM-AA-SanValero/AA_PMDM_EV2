package com.svalero.cybershopapp.model;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.content.Context;

import androidx.room.Room;

import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

public class ClientDetailsModel implements ClientDetailsContract.Model {

    private Context context;

    public ClientDetailsModel(Context context) {
        this.context = context;
    }

    @Override
    public Client loadClient(String name) {
        final AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();

        return database.clientDao().getByName(name);
    }
}
