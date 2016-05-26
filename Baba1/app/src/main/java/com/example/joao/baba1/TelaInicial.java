package com.example.joao.baba1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TelaInicial extends AppCompatActivity {

    private Button btn_inicial_crianca; //Botão para abrir a tela criança
    private Button btn_inicial_pais;    //Botão para abrir a tela dos pais


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        //Captura os Views pelo ID
        btn_inicial_crianca = (Button) this.findViewById(R.id.btn_Inicial_Crianca);
        btn_inicial_pais = (Button) this.findViewById(R.id.btn_inicial_pais);

        //Adiciona o evento de click dos botões
        btn_inicial_crianca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, TelaCrianca.class));
            }
        });

        //Adiciona o evento de click dos botões
        btn_inicial_pais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A fazer: Mudar para tela do modo pais
                Log.d("TAG-Joao", "Click em  btn_inicial_pais");
            }
        });

    }
}
