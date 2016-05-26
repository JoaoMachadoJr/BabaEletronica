package com.example.joao.baba1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Joao on 19/05/2016.
 */
public class ServiceCrianca extends Service {
    static String nome;
    static TelaCrianca tela;
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
        //Se já havia uma thread rodando, mata ela
        if (t!=null){
            try {
                SoundMeter.stop();
                SoundMeter.mRecorder=null;

            } catch (Exception e) {
                e.printStackTrace();
            }
            t.interrupt();
            t=null;
        }
        //Inicia uma thread rodando em background para ouvir o ambiente
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (t!=null && !t.isInterrupted()){
                    try {
                        d =SoundMeter.ouvir(2000);
                        Log.d("TAG-Joao","VOLUME="+d);
                        tela.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(tela,"VOLUME= "+d,Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e){
                        Log.e("TAG-Joao","ServiceCrianca",e);

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
        try {
            SoundMeter.t.interrupt();
            SoundMeter.stop();
            SoundMeter.mRecorder=null;

        } catch (Exception e) {
            Log.e("TAG-Joao","Exceção "+SoundMeter.t.isInterrupted(),e);
        }
        t.interrupt();
        //Log.e("TAG-Joao","Passou aqui: "+t.isAlive()+"   "+SoundMeter.t.isInterrupted());
        t=null;
        super.onDestroy();

    }

}
