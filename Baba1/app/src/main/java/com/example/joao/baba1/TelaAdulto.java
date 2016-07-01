package com.example.joao.baba1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TelaAdulto extends AppCompatActivity {

    private Button btnServicoAdulto;    //Botão para iniciar o serviço pais
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adulto);

        btnServicoAdulto = (Button) this.findViewById(R.id.btnServicoAdulto);

        //Evento de click do botão
        btnServicoAdulto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG-Lucas","Click no botao iniciar");
                ServiceAdulto.tela=TelaAdulto.this;

                startService(new Intent(TelaAdulto.this,ServiceAdulto.class));
                //System.exit(0);
            }
        });


    }
}
