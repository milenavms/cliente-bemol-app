package com.milena.clientebemol.atividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.milena.clientebemol.R;
import com.milena.clientebemol.modelos.Login;
import com.milena.clientebemol.modelos.LoginRetorno;
import com.milena.clientebemol.network.InterfaceApi;
import com.milena.clientebemol.network.RetrofitClientInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.milena.clientebemol.utils.Contants.HOST;
import static com.milena.clientebemol.utils.Contants.IP_PADRAO;
import static com.milena.clientebemol.utils.Contants.NOME;
import static com.milena.clientebemol.utils.Contants.SHARED_PREFERENCE;
import static com.milena.clientebemol.utils.Contants.SHARED_PREFERENCE_HOST;
import static com.milena.clientebemol.utils.Contants.SOBRENOME;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private TextView tvCadastro;
    private ImageView ivSetting;
    private Button btEntrar;
    private LinearLayout llEntrando;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private SharedPreferences spHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        spHost = getSharedPreferences(SHARED_PREFERENCE_HOST, MODE_PRIVATE);

        bindCampos();

        redirecionaSeLogado();

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                entrar(new Login(email, senha));
            }
        });

        tvCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(i);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ConfiguracaoActivity.class);
                startActivity(i);
            }
        });

    }

    private void entrar(final Login login) {

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(buscaIp());
        InterfaceApi api = retrofit.create(InterfaceApi.class);

        Call<LoginRetorno> call = api.logar(login);
        mostraCarregamento();

        call.enqueue(new Callback<LoginRetorno>() {
            @Override
            public void onResponse(Call<LoginRetorno> call, Response<LoginRetorno> response) {
                escondeCarregamento();
                if(response.isSuccessful()) {
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    gravaUsuarioLogadoLocal(response.body().getCliente().getNome(), response.body().getCliente().getSobrenome());
                    startActivity(i);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRetorno> call, Throwable t) {
                escondeCarregamento();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCampos(){
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        btEntrar = findViewById(R.id.bt_entrar);
        ivSetting = findViewById(R.id.iv_setting);
        tvCadastro = findViewById(R.id.tv_registrarse);
        llEntrando = findViewById(R.id.ll_entrando);
        progressBar = findViewById(R.id.pb_entrando);
    }

    private void gravaUsuarioLogadoLocal(String nome, String sobrenome) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NOME, nome);
        editor.putString(SOBRENOME, sobrenome);
        editor.apply();
        editor.commit();
    }

    private void mostraCarregamento() {
        llEntrando.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void escondeCarregamento() {
        llEntrando.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void redirecionaSeLogado() {
        String nome = sp.getString(NOME, "");
        if(!nome.isEmpty()){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    private String buscaIp(){
        return spHost.getString(HOST, IP_PADRAO);
    }
}