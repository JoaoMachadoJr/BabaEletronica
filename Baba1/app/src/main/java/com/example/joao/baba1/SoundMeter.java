package com.example.joao.baba1;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joao on 19/05/2016.
 */
public class SoundMeter{

    public static MediaRecorder mRecorder = null;
    public static boolean rodando=false;


    private static final int sampleRate = 8000;
    private AudioRecord audio;
    private int bufferSize;
    private double lastLevel = 0;
    public static Thread t;
    private static final int SAMPLE_DELAY = 75;
    public static double maior=-1;
    public static ArrayList<Double> lista = new ArrayList<Double>();




    public static void start() throws IOException {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            mRecorder.prepare();
            mRecorder.start();
        }
    }

    public static void stop(){
        if (mRecorder != null) {
//            mRecorder.wait(1000);
            mRecorder.stop();
            mRecorder.release();

        }
    }
    public static double getAmplitude(){
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }
    public static double ouvir(double tempo){
        t= new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    SoundMeter.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        if (!rodando){
            t.start();
            rodando=true;
        }
        try {
            Thread.sleep((long) tempo);
        } catch (InterruptedException e) {
            t.interrupt();
            t=null;

            //Log.e("TAG-Joao","Exceção",e);
            rodando=false;
            Thread.currentThread().interrupt();
            return 0;
        }
        t.interrupt();
        rodando=false;
       // SoundMeter.stop();
        return SoundMeter.getAmplitude();

    }


}

