package com.example.joao.baba1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class TelaAdulto extends AppCompatActivity {

    private Switch swt_adulto_ligado;
    private Switch swt_adulto_descoberta;
    private TextView tvw_adulto_ip;
    private TextView tvw_adulto_nome;
    private RelativeLayout layout_adulto_manual;
    private Button btn_adulto_salvar;
    private TextView tvw_adulto_busca;
    private Animation animation;//Animação para descoberta de rede

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adulto);

        //Configurações para utilizar a animação das letras piscando
        animation = new AlphaAnimation(1, 0); // Altera alpha de visível a invisível
        animation.setDuration(1800); // duração em milisegundos
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); // Repetir infinitamente
        animation.setRepeatMode(Animation.REVERSE); //Inverte a animação no final para que o botão vá desaparecendo

        //Captura os Views pelo ID
        swt_adulto_ligado = (Switch) this.findViewById(R.id.swt_adulto_ligado);
        swt_adulto_descoberta = (Switch) this.findViewById(R.id.swt_adulto_descoberta);
        tvw_adulto_ip = (TextView) this.findViewById(R.id.tvw_adulto_ip);
        layout_adulto_manual = (RelativeLayout) this.findViewById(R.id.layout_adulto_manual);
        btn_adulto_salvar = (Button) this.findViewById(R.id.btn_adulto_salvar);
        tvw_adulto_busca = (TextView) this.findViewById(R.id.tvw_adulto_busca);
        tvw_adulto_nome = (TextView) this.findViewById(R.id.tvw_adulto_nome);

        //Adiciona evento de toggle do switch para descoberta de rede Manual/Auto
        swt_adulto_descoberta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){//Descoberta manual
                    //Exibe campos para IP e porta
                    //layout_adulto_manual.setVisibility(View.VISIBLE);
                    tvw_adulto_ip.setEnabled(true);
                    tvw_adulto_nome.setEnabled(true);
                    btn_adulto_salvar.setEnabled(true);
                    tvw_adulto_busca.setVisibility(View.INVISIBLE);
                    tvw_adulto_busca.clearAnimation();

                } else{//Descoberta automática
                    //Esconde campos para IP e Porta
                    tvw_adulto_ip.setEnabled(false);
                    tvw_adulto_nome.setEnabled(false);
                    btn_adulto_salvar.setEnabled(false);
                    tvw_adulto_busca.setVisibility(View.VISIBLE);
                    tvw_adulto_busca.startAnimation(animation);

                    //Aqui exibe a lista de dispositivos encontrados
                    Log.d("tag-Joao-TelaAdulto","Vou imprimir o resultado da busca automatica");
                    ArrayList<Dispositivo> lista = ServiceAdulto.buscaAutomatica();

                    for (int i=0; i<lista.size(); i++){
                        Log.d("TAG-joao","Busca-> "+lista.get(i).nome+" - "+lista.get(i).ip);
                    }
                }
            }
        });

        //botao valvar
        btn_adulto_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ServiceAdulto.ipCrianca=formatoIp(tvw_adulto_ip.getText().toString());
                //tvw_adulto_ip.setText(formatoIp(tvw_adulto_ip.getText().toString()));
                ServiceAdulto.ipCrianca=tvw_adulto_ip.getText().toString();
                tvw_adulto_ip.setText(tvw_adulto_ip.getText().toString());
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

    }

    /*
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
    */
}
