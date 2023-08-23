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
@Entity
public class Repair {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo
    private String component;

    @ColumnInfo
    private String price;

    @ColumnInfo
    private String shippingAddress;

    @ColumnInfo
    private Date shipmentDate;

    @ColumnInfo
    private Date repairedDate;

    public Repair(String component, String price, String shippingAddress, Date shipmentDate, Date repairedDate) {
        this.component = component;
        this.price = price;
        this.shippingAddress = shippingAddress;
        this.shipmentDate = shipmentDate;
        this.repairedDate = repairedDate;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Date getRepairedDate() {
        return repairedDate;
    }

    public void setRepairedDate(Date repairedDate) {
        this.repairedDate = repairedDate;
    }
}
