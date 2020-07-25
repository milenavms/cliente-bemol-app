package com.milena.clientebemol.atividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.milena.clientebemol.R;
import com.milena.clientebemol.network.RetrofitClientInstance;

import static com.milena.clientebemol.utils.Contants.HOST;
import static com.milena.clientebemol.utils.Contants.IP_PADRAO;
import static com.milena.clientebemol.utils.Contants.SHARED_PREFERENCE_HOST;

public class ConfiguracaoActivity extends AppCompatActivity {

    private EditText etHost;
    private Button btGravar, btCancelar, btRestaurar ;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        sp = getSharedPreferences(SHARED_PREFERENCE_HOST, MODE_PRIVATE);

        bindCampos();

        carregarIp();

        btGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etHost.getText().toString().trim().isEmpty()) {
                    gravarHost(etHost.getText().toString());
                    RetrofitClientInstance.destroirRetrofit();
                    Toast.makeText(ConfiguracaoActivity.this, "Gravado com sucesso", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ConfiguracaoActivity.this, "Este campo não pode ser vazio", Toast.LENGTH_LONG).show();
                }
            }
        });

        btRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparHost();
                RetrofitClientInstance.destroirRetrofit();
                Toast.makeText(ConfiguracaoActivity.this, "Restaurado com sucesso", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void bindCampos() {
        etHost = findViewById(R.id.et_host);
        btGravar = findViewById(R.id.bt_gravar);
        btRestaurar = findViewById(R.id.bt_restaurar);
        btCancelar = findViewById(R.id.bt_cancelar);
    }

    private void gravarHost(String ip) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(HOST, ip);
        editor.apply();
        editor.commit();
    }

    private void limparHost() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    private void carregarIp() {
        String ip = sp.getString(HOST, IP_PADRAO);

        if(!ip.equals(IP_PADRAO)){
            etHost.setText(ip);
        }
    }



}