package com.svalero.cybershopapp.api;

import static com.svalero.cybershopapp.api.Constants.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.cybershopapp.util.LocalDateAdapter;

import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CybershopApi {
    public static CybershopApiInterface buildInstance() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(CybershopApiInterface.class);
    }
}
