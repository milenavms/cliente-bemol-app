package com.milena.clientebemol.atividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.milena.clientebemol.R;

import static com.milena.clientebemol.utils.Contants.NOME;
import static com.milena.clientebemol.utils.Contants.SHARED_PREFERENCE;
import static com.milena.clientebemol.utils.Contants.SOBRENOME;

public class HomeActivity extends AppCompatActivity {
    private Button btSair;
    private TextView tvPrimeiroNome, tvSobrenome;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);

        bindCampos();

        mostraUsuarioLogado();

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                removeUsuarioLogado();
                startActivity(i);
                finish();
            }
        });


    }

    private void bindCampos(){
        btSair = findViewById(R.id.bt_sair);
        tvPrimeiroNome = findViewById(R.id.tv_primeiro_nome);
        tvSobrenome = findViewById(R.id.tv_sobrenome);
    }

    private void mostraUsuarioLogado() {
        tvPrimeiroNome.setText(sp.getString(NOME, "Vazio"));
        tvSobrenome.setText( sp.getString(SOBRENOME, "Vazio"));
    }

    private void removeUsuarioLogado() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }
}