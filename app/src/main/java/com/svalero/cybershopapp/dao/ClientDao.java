package com.svalero.cybershopapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.cybershopapp.domain.Client;

import java.sql.Date;
import java.util.List;

@Dao
public interface ClientDao {

    @Query("SELECT * FROM client")
    List<Client> getAll();
    @Query("SELECT * FROM client WHERE name = :name")
    Client getByName(String name);
    @Query("DELETE FROM client WHERE name = :name")
    void deleteByName(String name);
    @Query("UPDATE client SET name = :newName, surname = :newSurname, number = :newNumber," +
            "register_date = :newDate,vip = :status WHERE name = :currentName")
    void updateByName(String currentName, String newName, String newSurname, String newNumber
            , Date newDate, boolean status);

    @Insert
    void insert(Client client);
    @Delete
    void delete(Client client);
    @Update
    void update(Client client);

}

