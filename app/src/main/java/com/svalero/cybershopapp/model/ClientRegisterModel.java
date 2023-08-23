package com.svalero.cybershopapp.model;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;

import com.svalero.cybershopapp.contract.ClientRegisterContract;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.sql.Date;

public class  ClientRegisterModel implements ClientRegisterContract.Model {

    private Context context;
    public ClientRegisterModel(Context context) {
        this.context = context;
    }
    @Override
    public boolean registerClient(Client client) {

        try {
            final AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_CLIENTS)
                    .allowMainThreadQueries().build();
            database.clientDao().insert(client);
            return true;
        } catch (SQLiteConstraintException sce){
            sce.printStackTrace();
            return false;
        }

    }
}
