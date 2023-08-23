package com.svalero.cybershopapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.domain.Repair;

import java.sql.Date;
import java.util.List;
@Dao
public interface RepairDao {


    @Query("SELECT * FROM repair")
    List<Repair> getAll();
    @Query("SELECT * FROM repair WHERE component = :component")
    Repair getByComponent(String component);
    @Query("DELETE FROM repair WHERE component = :component")
    void deleteByComponent(String component);
    @Query("UPDATE repair SET component = :newComponent, price = :newPrice, " +
            "shippingAddress = :newShipAddress, shipmentDate = :newShipDate, " +
            "repairedDate = :newRepairedDate WHERE component = :currentComponent")
    void updateByComponent(String currentComponent, String newComponent, String newPrice, String newShipAddress,
                      Date newShipDate, Date newRepairedDate);
    @Insert
    void insert(Repair repair);
    @Delete
    void delete(Repair repair);
    @Update
    void update(Repair repair);
}
