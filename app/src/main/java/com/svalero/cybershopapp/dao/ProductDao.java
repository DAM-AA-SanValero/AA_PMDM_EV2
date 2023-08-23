package com.svalero.cybershopapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;

import java.util.List;
@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAll();
    @Query("SELECT * FROM product WHERE name = :name")
    Product getByName(String name);
    @Query("DELETE FROM product WHERE name = :name")
    void deleteByName(String name);
    @Query("UPDATE product SET name = :newName, type = :newType, price = :newPrice," +
            " origin = :newOrigin, inStock = :availability WHERE name = :currentName")
    void updateByName(String currentName, String newName, String newType, String newPrice,
                      String newOrigin, boolean availability);
    @Insert
    void insert(Product product);
    @Delete
    void delete(Product product);
    @Update
    void update(Product product);

}
