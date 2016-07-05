package com.example.joao.baba1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class TelaAdulto extends AppCompatActivity {

    private Switch swt_adulto_ligado;
    private Switch swt_adulto_descoberta;
    private TextView tvw_adulto_ip;
    private TextView tvw_adulto_porta;
    private RelativeLayout layout_adulto_manual;
    private Button btn_adulto_salvar;
    private TextView tvw_adulto_iplocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adulto);

        //Captura os Views pelo ID
        swt_adulto_ligado = (Switch) this.findViewById(R.id.swt_adulto_ligado);
        swt_adulto_descoberta = (Switch) this.findViewById(R.id.swt_adulto_descoberta);
        tvw_adulto_ip = (TextView) this.findViewById(R.id.tvw_adulto_ip);
        tvw_adulto_porta = (TextView) this.findViewById(R.id.tvw_adulto_nome);
        layout_adulto_manual = (RelativeLayout) this.findViewById(R.id.layout_adulto_manual);
        btn_adulto_salvar = (Button) this.findViewById(R.id.btn_adulto_salvar);

        tvw_adulto_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Adiciona evento de toggle do switch para descoberta de rede Manual/Auto
        swt_adulto_descoberta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false){//Descoberta manual
                    //Exibe campos para IP e porta
                    layout_adulto_manual.setVisibility(View.VISIBLE);
                    tvw_adulto_iplocal.setText("Seu IP local é: "+ServiceAdulto.ipCrianca);

                } else{//Descoberta automática
                    //Esconde campos para IP e Porta
                    layout_adulto_manual.setVisibility(View.INVISIBLE);

                }
            }
        });

        //botao valvar
        btn_adulto_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceAdulto.ipCrianca=formatoIp(tvw_adulto_ip.getText().toString());
                tvw_adulto_ip.setText(formatoIp(tvw_adulto_ip.getText().toString()));
            }
        });

        //Evento de toggle do Switch que liga/desliga o serviço criança
        swt_adulto_ligado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ //Se estiver ligando
                    try {
                        Log.d("TAG-Lucas","Click no botao iniciar");
                        ServiceAdulto.tela=TelaAdulto.this;
                        startService(new Intent(TelaAdulto.this,ServiceAdulto.class));

                    } catch (Exception e) {
                        Log.e("TAG-Joao","Exceção",e);
                    }
                } else{ //Se estiver desligando
                    stopService(new Intent(TelaAdulto.this,ServiceAdulto.class));
                }
            }
        });
        
        /*
        //Evento de click do botão
        btnServicoAdulto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG-Lucas","Click no botao iniciar");
                ServiceAdulto.tela=TelaAdulto.this;
                startService(new Intent(TelaAdulto.this,ServiceAdulto.class));
                //System.exit(0);
            }
        });*/


    }

    public static String formatoIp(String entrada){
        String saida="";
        int i=0;
        while (i<entrada.length()){
            saida+=entrada.charAt(i);
            if ( (i+1)%3==0 && i!=0 && i!=(entrada.length()-1) ){
                saida=saida+'.';
            }
            i++;
        }
        return saida;
    };
}
