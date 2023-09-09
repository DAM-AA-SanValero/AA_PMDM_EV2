package com.svalero.cybershopapp.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repair {

    private long id;
    private String component;
    private float price;
    private String shippingAddress;
    private LocalDate shipmentDate;
    private LocalDate repairedDate;

    public Repair(String component, float price, String shippingAddress,
                  LocalDate shipmentDate, LocalDate repairedDate) {
        this.component = component;
        this.price = price;
        this.shippingAddress = shippingAddress;
        this.shipmentDate = shipmentDate;
        this.repairedDate = repairedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDate getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public LocalDate getRepairedDate() {
        return repairedDate;
    }

    public void setRepairedDate(LocalDate repairedDate) {
        this.repairedDate = repairedDate;
    }
}
