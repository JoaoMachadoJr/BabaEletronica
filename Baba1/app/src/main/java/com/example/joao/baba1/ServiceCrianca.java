package com.example.joao.baba1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Joao on 19/05/2016.
 */
public class ServiceCrianca extends Service {
    static String nome;
    static TelaCrianca tela;
    static double d;
    static Thread threadServico;
    static Thread threadNovosOuvintes;
    static ArrayList<Ouvinte> ouvintes = new ArrayList<>();
    ServerSocket welcomeSocket = null;
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
        if (threadServico!=null){
            try {
                SoundMeter.stop();
                SoundMeter.mRecorder=null;

            } catch (Exception e) {
                e.printStackTrace();
            }
            threadServico.interrupt();
            threadServico=null;
        }

        //Inicia uma thread rodando em background para ouvir o ambiente
        Runnable r = new Runnable() {
            @Override
            public void run() {
                escutaNovosOuvintes();
                while (threadServico!=null && !threadServico.isInterrupted()){
                    try {
                        d =SoundMeter.ouvir(2000);
                        Log.d("TAG-Joao","VOLUME="+d);

                        for (int i=0; i<ouvintes.size(); i++){  //para cada um dos ouvintes
                            if (ouvintes.get(i).sensibilidade<=d){
                                final Ouvinte o=ouvintes.get(i).clone();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        notificaOuvinte(d,o);
                                    }
                                };
                                new Thread(r).start();

                            }
                        }
                    } catch (Exception e){
                        Log.e("TAG-Joao","ServiceCrianca",e);

                    }
                }
            }
        };
        threadServico=new Thread(r);

        threadServico.start();
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
        threadServico.interrupt();
        //Log.e("TAG-Joao","Passou aqui: "+t.isAlive()+"   "+SoundMeter.t.isInterrupted());
        threadServico=null;
        super.onDestroy();

    }

    //Metodo que cria uma Thread que espera pas chegarem
    public void escutaNovosOuvintes(){
        paraEscutaNovosOuvintes();  //Se ja estava escutando antes, pare de escutar
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TAG-Joao-ServicoCrianca","Vou comecar a procurar novos ouvintes");
                    ObjectInputStream inStream=null;

                    while (true) {
                        welcomeSocket = new ServerSocket(6789);
                        Socket connectionSocket = welcomeSocket.accept();
                        inStream = new ObjectInputStream(connectionSocket.getInputStream());
                        Ouvinte pai = (Ouvinte) inStream.readObject();
                        synchronized (ouvintes){
                            ouvintes.add(pai);
                        }
                        Log.d("TAG-joao-ServiceCrianca", "Adicionou a lista de ouvintes: "+pai.toString());
                        connectionSocket.close();
                        welcomeSocket.close();
                    }
                } catch (Exception e){
                    Log.d("TAG-Joao", e.toString());
                    paraEscutaNovosOuvintes();
                }
            }
        };
        threadNovosOuvintes=new Thread(r);
        threadNovosOuvintes.start();

    }

    //Metodo que mata a thread queescuta novos ouvintes
    public void paraEscutaNovosOuvintes(){
        if (threadNovosOuvintes==null){
            return;
        }
        threadNovosOuvintes.interrupt();
        threadNovosOuvintes=null;
        synchronized (ouvintes){
            ouvintes=new ArrayList<>();
            try {
                if (welcomeSocket!=null && !welcomeSocket.isClosed())
                    welcomeSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void notificaOuvinte(double d, Ouvinte ouvinte){
        try {
            Socket clientSocket = new Socket(ouvinte.ip, ouvinte.port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(""+d);
            clientSocket.close();
            Log.d("TAG-joao-ServicoCrianca","Notifiquei o pai "+ouvinte.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
