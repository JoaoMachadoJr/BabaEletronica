package com.example.joao.baba1;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class TelaCrianca extends AppCompatActivity {




    //declaracao de variaveis
    private Switch swt_crianca_ligado;
    private Switch swt_crianca_descoberta;
    private TextView tvw_crianca_ip;
    private TextView tvw_crianca_porta;
    private RelativeLayout layout_crianca_manual;
    private Button btn_crianca_salvar;
    private TextView tvw_crianca_iplocal;
    SoundMeter sm = new SoundMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_crianca);

        //Captura os Views pelo ID
        swt_crianca_ligado = (Switch) this.findViewById(R.id.swt_crianca_ligado);
        swt_crianca_descoberta = (Switch) this.findViewById(R.id.swt_crianca_descoberta);
        tvw_crianca_ip = (TextView) this.findViewById(R.id.tvw_crianca_ip);
        tvw_crianca_porta = (TextView) this.findViewById(R.id.tvw_crianca_porta);
        layout_crianca_manual = (RelativeLayout) this.findViewById(R.id.layout_crianca_manual);
        btn_crianca_salvar = (Button) this.findViewById(R.id.btn_crianca_salvar);
        tvw_crianca_iplocal = (TextView) this.findViewById(R.id.tvw_crianca_iplocal);

        //Exibe o IP local no textview adequado
        ConfigCrianca.meu_ip=getIpAddress();
        tvw_crianca_iplocal.setText("Seu IP local é: "+ConfigCrianca.meu_ip);

        //Adiciona evento de toggle dos switches
        swt_crianca_descoberta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==ConfigCrianca._DESCOBERTA_MANUAL){
                    //Exibe campos para IP e porta
                    layout_crianca_manual.setVisibility(View.VISIBLE);
                    ConfigCrianca.meu_ip=getIpAddress();
                    tvw_crianca_iplocal.setText("Seu IP local é: "+ConfigCrianca.meu_ip);

                } else{
                    //Esconde campos para IP e Porta
                    layout_crianca_manual.setVisibility(View.INVISIBLE);
                }
            }
        });

        swt_crianca_ligado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Intent mServiceIntent = new Intent(TelaCrianca.this, ServiceCrianca.class);
                //startService(mServiceIntent);

                if (isChecked){
                    try {
                        sm= new SoundMeter();
                       // sm.start();
                        sm.onCreate();
                        sm.onResume();
                        Thread.sleep(1000);
                        sm.onPause();
                        Toast.makeText(TelaCrianca.this, "Volume: "+SoundMeter.maior, Toast.LENGTH_SHORT).show();
                        SoundMeter.maior=-1;
                    } catch (Exception e) {
                        Log.e("MINHATAG",e.toString());
                    }
                } else{
                   // sm.stop();
                   // Log.d("MINHATAG", "Terminou");
                   // double valor = sm.getAmplitude();
                    //Log.d("MINHATAG", "VALOR="+valor);
                   // sm.stop();
                }
            }
        });

        //Adiciona evento ao botao de salvar
        btn_crianca_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A Fazer: salvar alteracoes
            }
        });
    }


    //Funcao que peguei na internet, ela me retorna o endereço IPV4 local do celular
    public static String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("MEUERRO", ex.toString());
        }
        return null;
    }

}
