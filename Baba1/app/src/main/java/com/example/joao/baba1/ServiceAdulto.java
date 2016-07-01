package com.example.joao.baba1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ServiceAdulto extends Service{

    //static String nome;
    static TelaAdulto tela;
    static double d;
    static Thread t;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Log.d("TAG","Servico iniciou");
        //Se já havia uma thread rodando, mata ela
        if (t!=null){
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.interrupt();
            t=null;
        }
        //Inicia uma thread rodando em background
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (t!=null && !t.isInterrupted()){
                    try {
                        Log.d("TAG-Lucas","Serviço Iniciado");
                        tela.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(tela,"Serviço Iniciado",Toast.LENGTH_SHORT).show();
                            }
                        });
                        Thread.sleep(1000);


                    } catch (Exception e){
                        Log.e("TAG-Lucas","ServiceAdulto",e);

                    }
                }
            }
        };
        t=new Thread(r);

        t.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){    //Metodo chamado ao parar o Serviço, ele libera os recursos e mata a thread
        t.interrupt();
        //Log.e("TAG-Joao","Passou aqui: "+t.isAlive()+"   "+SoundMeter.t.isInterrupted());
        t=null;
        super.onDestroy();

    }

}
