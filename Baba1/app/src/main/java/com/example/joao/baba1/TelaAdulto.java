package com.example.joao.baba1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class TelaAdulto extends AppCompatActivity {

    private Switch swt_adulto_ligado;
    public Switch swt_adulto_descoberta;
    private TextView tvw_adulto_ip;
  //  private TextView tvw_adulto_nome;
 //   private RelativeLayout layout_adulto_manual;
    private Button btn_adulto_salvar;
  //  private TextView tvw_adulto_busca;
    private Animation animation;//Animação para descoberta de rede
    private SeekBar sensibilidade;
    private TextView progresso;
    public static int notificacao=0;
    public ArrayList<Dispositivo> lista;
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
   //     layout_adulto_manual = (RelativeLayout) this.findViewById(R.id.layout_adulto_manual);
        btn_adulto_salvar = (Button) this.findViewById(R.id.btn_adulto_salvar);
      // tvw_adulto_busca = (TextView) this.findViewById(R.id.tvw_adulto_busca);
      //  tvw_adulto_nome = (TextView) this.findViewById(R.id.tvw_adulto_nome);
        progresso=(TextView)findViewById(R.id.tv_sensibilidade);
        sensibilidade=(SeekBar)findViewById(R.id.seekBar);
        sensibilidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresso.setText("Sensibilidade: "+progress+"%");
                ServiceAdulto.sensibilidade=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Adiciona evento de toggle do switch para descoberta de rede Manual/Auto
        swt_adulto_descoberta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){//Descoberta manual
                    tvw_adulto_ip.setEnabled(true);
                    tvw_adulto_ip.setVisibility(View.VISIBLE);
                    btn_adulto_salvar.setEnabled(true);

                } else{//Descoberta automática
                    //Esconde campos para IP e Porta
                    tvw_adulto_ip.setEnabled(false);

                    btn_adulto_salvar.setEnabled(false);
                    tvw_adulto_ip.setVisibility(View.INVISIBLE);

                    //Aqui exibe a lista de dispositivos encontrados
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Log.d("tag-Joao-TelaAdulto","Vou imprimir o resultado da busca automatica");
                            lista = ServiceAdulto.buscaAutomatica();
                            Log.d("tag-Joao-TelaAdulto","passou aqui");

                            if(lista.size() > 0){
                                TelaAdulto.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PopupMenu popup = new PopupMenu(TelaAdulto.this,swt_adulto_descoberta);
                                        for (int i=0; i<lista.size(); i++){
                                            Log.d("TAG-joao","Busca-> "+lista.get(i).nome+" - "+lista.get(i).ip);
                                            popup.getMenu().add(i+"- "+lista.get(i).nome+" "+lista.get(i).ip);
                                        }
                                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                String s = item.getTitle().toString();
                                                String s2="";
                                                for (int i=0; i<s.length() && s.charAt(i)!='-';i++){
                                                    s2+=s.charAt(i);
                                                }
                                                ServiceAdulto.ipCrianca=lista.get(Integer.parseInt(s2)).ip;
                                                tvw_adulto_ip.setText(ServiceAdulto.ipCrianca);
                                                return true;
                                            }
                                        });
                                        popup.show();
                                    }
                                });

                            }
                            else{

                                TelaAdulto.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(TelaAdulto.this);
                                        alert.setTitle("Problema");
                                        alert.setMessage("Nenhum dispositivo encontrado!");
                                        alert.setPositiveButton("OK",null);
                                        alert.show();
                                        TelaAdulto.this.swt_adulto_descoberta.setChecked(false);
                                    }
                                });

                            }
                            Log.d("tag-Joao-TelaAdulto","ja terminei a busca automatica");
                        }
                    };
                    new Thread(r).start();


                }
            }
        });

        //botao valvar
        btn_adulto_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ServiceAdulto.ipCrianca=formatoIp(tvw_adulto_ip.getText().toString());
                //tvw_adulto_ip.setText(formatoIp(tvw_adulto_ip.getText().toString()));
                if (!TelaAdulto.this.swt_adulto_descoberta.isChecked()){
                    ServiceAdulto.ipCrianca=tvw_adulto_ip.getText().toString();
                    tvw_adulto_ip.setText(tvw_adulto_ip.getText().toString());
                }

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

    public void triggerNotification(String titulo, String descricao, boolean som) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notificacao);
        mBuilder.setContentTitle(titulo);
        mBuilder.setContentText(descricao);
        if(som){
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mBuilder.setSound(soundUri);

        }
        Notification notification = mBuilder.build();
        if(som){
            notification.flags |= Notification.FLAG_INSISTENT;
        }


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificacao++, notification);

// notificationID allows you to update the notification later on.

    }

    public void exibePopup(){
        PopupMenu popup = new PopupMenu(TelaAdulto.this,swt_adulto_descoberta);
        popup.getMenu().add("item1");
        popup.getMenu().add("item2");
        popup.getMenu().add("item3");
        popup.show();
    }
}
