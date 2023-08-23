package com.svalero.cybershopapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.svalero.cybershopapp.dao.ClientDao;
import com.svalero.cybershopapp.dao.ProductDao;
import com.svalero.cybershopapp.dao.RepairDao;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.util.DateConverter;

@Database(entities = {Client.class, Product.class, Repair.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClientDao clientDao();

    public abstract ProductDao productDao();

    public abstract RepairDao repairDao();

}


