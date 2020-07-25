package com.milena.clientebemol.network;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static String API_BASE_URL = ":8081/autenticacao/";
    private static String HTTP = "http://";
    private static String API_VIACEP = "http://viacep.com.br/ws/";
    private static Retrofit retrofit;
    private static Retrofit retrofitViaCep;
    private static Gson gson;
    private static SharedPreferences sp;

    public static Retrofit getRetrofitInstance(String ip) {
        if(retrofit == null){
            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(HTTP + ip + API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getRetrofitInstanceViaCep() {
        if(retrofitViaCep == null){
            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofitViaCep = new Retrofit.Builder()
                    .baseUrl(API_VIACEP)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofitViaCep;
    }

    public static void destroirRetrofit(){
        retrofit = null;
    }
}
