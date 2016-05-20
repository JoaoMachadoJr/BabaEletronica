package com.example.joao.baba1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Joao on 19/05/2016.
 */
public class ServiceCrianca extends Service {
    static String nome;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i=0;
        Log.d("MINHATAG1", "SERVICO INICIOU"+Thread.currentThread().getId());
        Thread_Crianca tc = new Thread_Crianca();
        Thread t = new Thread(tc);
        t.start();
        return START_NOT_STICKY;

        //return START_STICKY;
    }


}
