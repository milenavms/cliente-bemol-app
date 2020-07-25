package com.milena.clientebemol.atividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.milena.clientebemol.R;
import com.milena.clientebemol.modelos.CepApi;
import com.milena.clientebemol.modelos.Cliente;
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

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etSobrenome,etNascimento, etSexo;
    private EditText etDestinatario, etCep, etEndereco, etComplemento, etNumero, etBairro, etCidade,
            etEstado, etReferencia;
    private EditText etCelular;
    private EditText etEmail, etSenha;
    private Button btRegistrar;
    private SharedPreferences sp;
    private SharedPreferences spHost;
    private ProgressBar progressBar;
    private LinearLayout llLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        sp = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        spHost = getSharedPreferences(SHARED_PREFERENCE_HOST, MODE_PRIVATE);

        bindCampos();

        desabilitaCamposEndereco();

        etCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 8) {
                    buscaCep(etCep.getText().toString());
                } else {
                    limpaDadosEndereco();
                }
            }
        });


        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(camposValidos()) {
                    registrar(preparaParametros());
                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos *obrigatórios", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void buscaCep(String cep){
        Retrofit retrofit =  RetrofitClientInstance.getRetrofitInstanceViaCep();
        InterfaceApi api = retrofit.create(InterfaceApi.class);

        Call<CepApi> call = api.buscarCep(cep);
        mostraCarregamento();

        call.enqueue(new Callback<CepApi>() {
            @Override

            public void onResponse(Call<CepApi> call, Response<CepApi> response) {
                escondeCarregamento();
                if(response.isSuccessful()){
                    if(!response.body().isErro()) {
                        adicionaDadosEndereco(response.body());
                    } else {
                        limpaDadosEndereco();
                        Toast.makeText(CadastroActivity.this, "CEP Inváido", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(CadastroActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CepApi> call, Throwable t) {
                escondeCarregamento();
                Toast.makeText(CadastroActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void registrar(Cliente cliente) {
        Retrofit retrofit =  RetrofitClientInstance.getRetrofitInstance(buscaIp());
        InterfaceApi api = retrofit.create(InterfaceApi.class);

        mostraCarregamento();

        Call<LoginRetorno> call = api.registrar(cliente);

        call.enqueue(new Callback<LoginRetorno>() {
            @Override
            public void onResponse(Call<LoginRetorno> call, Response<LoginRetorno> response) {
                escondeCarregamento();
                if(response.isSuccessful()){
                    Intent i = new Intent(CadastroActivity.this, HomeActivity.class);
                    gravaUsuarioLogadoLocal(response.body().getCliente().getNome(), response.body().getCliente().getSobrenome());
                    startActivity(i);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(CadastroActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRetorno> call, Throwable t) {
                escondeCarregamento();
                Toast.makeText(CadastroActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCampos(){
        etNome = findViewById(R.id.et_nome);
        etSobrenome = findViewById(R.id.et_sobrenome);
        etNascimento = findViewById(R.id.et_nascimento);
        etSexo = findViewById(R.id.et_sexo);
        etDestinatario = findViewById(R.id.et_destinatario);
        etCep = findViewById(R.id.et_cep);
        etEndereco = findViewById(R.id.et_endereco);
        etComplemento = findViewById(R.id.et_complemento);
        etNumero = findViewById(R.id.et_numero);
        etBairro = findViewById(R.id.et_bairro);
        etCidade = findViewById(R.id.et_cidade);
        etEstado = findViewById(R.id.et_estado);
        etReferencia = findViewById(R.id.et_referencia);
        etCelular = findViewById(R.id.et_celular);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        btRegistrar = findViewById(R.id.bt_registrar);
        progressBar = findViewById(R.id.pb_registrando);
        llLoad = findViewById(R.id.ll_registrando);
    }

    private Cliente preparaParametros() {
        Cliente cliente = new Cliente();
        cliente.setNome(etNome.getText().toString());
        cliente.setSobrenome(etSobrenome.getText().toString());
        cliente.setNascimento(etNascimento.getText().toString());
        cliente.setSexo(etSexo.getText().toString());
        cliente.setDestinatario(etDestinatario.getText().toString());
        cliente.setCep(etCep.getText().toString());
        cliente.setEndereco(etEndereco.getText().toString());
        cliente.setComplemento(etComplemento.getText().toString());
        cliente.setNumero(Integer.parseInt(etNumero.getText().toString()));
        cliente.setBairro(etBairro.getText().toString());
        cliente.setCidade(etCidade.getText().toString());
        cliente.setEstado(etEstado.getText().toString());
        cliente.setReferencia(etReferencia.getText().toString());
        cliente.setCelular(etCelular.getText().toString());
        cliente.setEmail(etEmail.getText().toString());
        cliente.setSenha(etSenha.getText().toString());

        return cliente;
    }

    private boolean camposValidos() {
        if(etNome.getText().toString().trim().isEmpty())
            return false;
        if(etSobrenome.getText().toString().trim().isEmpty())
            return false;
        if(etNascimento.getText().toString().trim().isEmpty())
            return false;
        if(etSexo.getText().toString().trim().isEmpty())
            return false;
        if(etDestinatario.getText().toString().trim().isEmpty())
            return false;
        if(etCep.getText().toString().trim().isEmpty())
            return false;
        if(etEndereco.getText().toString().trim().isEmpty())
            return false;
        if(etNumero.getText().toString().trim().isEmpty())
            return false;
        if(etBairro.getText().toString().trim().isEmpty())
            return false;
        if(etCidade.getText().toString().trim().isEmpty())
            return false;
        if(etEstado.getText().toString().trim().isEmpty())
            return false;
        if(etCidade.getText().toString().trim().isEmpty())
            return false;
        if(etCelular.getText().toString().trim().isEmpty())
            return false;
        if(etEmail.getText().toString().trim().isEmpty())
            return false;
        if(etSenha.getText().toString().trim().isEmpty())
            return false;

        return true;
    }

    private void adicionaDadosEndereco(CepApi cepApi){
        etEndereco.setText(cepApi.getLogradouro());
        etComplemento.setText(cepApi.getComplemento());
        etBairro.setText(cepApi.getBairro());
        etCidade.setText(cepApi.getLocalidade());
        etEstado.setText(cepApi.getUf());
    }

    private void desabilitaCamposEndereco(){
        etEndereco.setEnabled(false);
        etComplemento.setEnabled(false);
        etBairro.setEnabled(false);
        etCidade.setEnabled(false);
        etEstado.setEnabled(false);
    }

    private void limpaDadosEndereco(){
        etEndereco.setText("");
        etComplemento.setText("");
        etBairro.setText("");
        etCidade.setText("");
        etEstado.setText("");
    }

    private void gravaUsuarioLogadoLocal(String nome, String sobrenome) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NOME, nome);
        editor.putString(SOBRENOME, sobrenome);
        editor.apply();
        editor.commit();
    }

    private void mostraCarregamento() {
        llLoad.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void escondeCarregamento() {
        llLoad.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private String buscaIp(){
        return spHost.getString(HOST, IP_PADRAO);
    }


}