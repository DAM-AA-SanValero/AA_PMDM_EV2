package com.svalero.cybershopapp.domain;

import androidx.room.*;


import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String surname;
    @ColumnInfo
    private String number;
    @ColumnInfo
    private Date register_date;
    @ColumnInfo
    private boolean vip;
    @ColumnInfo
    private double latitude;
    @ColumnInfo
    private double longitude;
    @ColumnInfo
    private byte[] image;

    public Client(String name, String surname, String number, Date register_date, boolean vip, double latitude, double longitude, byte[] image) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.register_date = register_date;
        this.vip = vip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
