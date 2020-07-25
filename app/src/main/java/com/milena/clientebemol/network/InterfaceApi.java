package com.milena.clientebemol.network;

import com.milena.clientebemol.modelos.CepApi;
import com.milena.clientebemol.modelos.Cliente;
import com.milena.clientebemol.modelos.Login;
import com.milena.clientebemol.modelos.LoginRetorno;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceApi {

    @POST("entrar")
    Call<LoginRetorno> logar(@Body Login login);

    @POST("registro")
    Call<LoginRetorno> registrar(@Body Cliente cliente);

    @GET("{cep}/json")
    Call<CepApi> buscarCep(@Path("cep") String cep);

}
