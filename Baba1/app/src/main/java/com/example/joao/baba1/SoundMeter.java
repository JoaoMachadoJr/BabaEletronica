package com.example.joao.baba1;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joao on 19/05/2016.
 */
public class SoundMeter{

    private MediaRecorder mRecorder = null;

    public void start() throws IOException {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            mRecorder.prepare();
            mRecorder.start();
            Log.d("MINHATAG2","mRecorder.start() , NULL=="+(mRecorder==null));
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
           // mRecorder = null;
           // Log.d("MINHATAG2","mRecorder.release() , NULL=="+(mRecorder==null));
        }
    }

    public double getAmplitude() {
        if (mRecorder != null){

            Log.d("MINHATAG2","mRecorder.start() , NULL=="+(mRecorder==null));
            return  mRecorder.getMaxAmplitude();}
        else
            return -1;

    }


    public double ouvir(long tempo) throws IOException, InterruptedException {
        synchronized (Thread.currentThread()){
            Log.d("MINHATAG1","comecou a ouvir");
            Timer t = new Timer();
            this.start();
            Thread.currentThread().wait(tempo);
            this.stop();
            Log.d("MINHATAG1", "Terminou de ouvir");
            return this.getAmplitude();

        }

    }

    private static final int sampleRate = 8000;
    private AudioRecord audio;
    private int bufferSize;
    private double lastLevel = 0;
    private Thread thread;
    private static final int SAMPLE_DELAY = 75;
    public static double maior=-1;

    protected void onCreate() {

        try {
            bufferSize = AudioRecord
                    .getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }
    }

    protected void onResume() {
        audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audio.startRecording();
        thread = new Thread(new Runnable() {
            public void run() {
                while(thread != null && !thread.isInterrupted()){
                    //Let's make the thread sleep for a the approximate sampling time
                    try{Thread.sleep(SAMPLE_DELAY);}catch(InterruptedException ie){ie.printStackTrace();}
                    readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable

                    Log.d("MINHATAG3","LASTLEVEL="+lastLevel);
                    if (lastLevel>maior){
                        maior=lastLevel;
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Functionality that gets the sound level out of the sample
     */
    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null) {

                // Sense the voice...
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void onPause() {

        thread.interrupt();
        thread = null;
        try {
            if (audio != null) {
                audio.stop();
                audio.release();
                audio = null;
            }
        } catch (Exception e) {e.printStackTrace();}
    }


}

