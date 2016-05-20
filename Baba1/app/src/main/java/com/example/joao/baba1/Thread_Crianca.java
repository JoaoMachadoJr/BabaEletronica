package com.example.joao.baba1;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joao on 19/05/2016.
 */
public class Thread_Crianca implements Runnable {

    @Override
    public void run() {
        for (int i=0; i<1; i++){
            //SoundMeter sm = new SoundMeter();
            try {

                Log.d("MINHATAG1","VOLUME: "+new SoundMeter().ouvir(10000));
            } catch (IOException e) {
                Log.e("MINHATAG1", e.toString());
            } catch (InterruptedException e) {
                Log.e("MINHATAG1", e.toString());
            }
        }
    }


}
