package com.example.joao.baba1;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
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
    static Thread threadNovosdispositivos;
    static ArrayList<Dispositivo> dispositivos = new ArrayList<>();
    static ArrayList<Thread> t_dispositivos = new ArrayList<>();
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
                escutaNovosdispositivos();
                while (threadServico!=null && !threadServico.isInterrupted()){
                    try {
                        d =SoundMeter.ouvir(2000);
                        Log.d("TAG-Joao","VOLUME="+d);

                        for (int i = 0; i< dispositivos.size(); i++){  //para cada um dos dispositivos
                            if (dispositivos.get(i).sensibilidade<=d){
                                final Dispositivo o= dispositivos.get(i).clone();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        notificadispositivo(d,o);
                                    }
                                };
                                Thread t=new Thread(r);
                                t.start();
                                t_dispositivos.add(t);

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
        paraEscutaNovosdispositivos();
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
        for (int i=0; i<t_dispositivos.size();i++){
            t_dispositivos.get(i).interrupt();
        }
        t_dispositivos=new ArrayList<>();
        super.onDestroy();

    }

    //Metodo que cria uma Thread que espera pas chegarem
    public void escutaNovosdispositivos(){
        paraEscutaNovosdispositivos();  //Se ja estava escutando antes, pare de escutar
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TAG-Joao-ServicoCrianca","Vou comecar a procurar novos dispositivos");
                    ObjectInputStream inStream=null;

                    while (true) {
                        welcomeSocket = new ServerSocket(6789);
                        Socket connectionSocket = welcomeSocket.accept();
                        Mensagem msg = Mensagem.ler(connectionSocket);
                        //requisição de dispositivo
                        if (msg.tipo.equals("requisição de dispositivo")){
                            Dispositivo pai=(Dispositivo)msg.parametros.get("dispositivo");
                            synchronized (dispositivos){
                                dispositivos.add(pai);
                            }
                            Log.d("TAG-joao-ServiceCrianca", "Adicionou a lista de dispositivos: "+pai.toString());
                        }
                        //ping
                        else if (msg.tipo.equals("ping")){
                            Mensagem nova = new Mensagem();
                            nova.tipo = "pong";
                            Mensagem.escrever(nova, connectionSocket);
                        } else if (msg.tipo.equals("busca")){
                            Mensagem nova = new Mensagem("busca");
                            nova.parametros.put("nome", Build.MODEL);
                            Mensagem.escrever(nova, connectionSocket);
                        }

                        connectionSocket.close();
                        welcomeSocket.close();
                    }
                } catch (Exception e){
                    Log.d("TAG-Joao", e.toString());
                    paraEscutaNovosdispositivos();
                }
            }
        };
        threadNovosdispositivos=new Thread(r);
        threadNovosdispositivos.start();

    }

    //Metodo que mata a thread queescuta novos dispositivos
    public void paraEscutaNovosdispositivos(){
        if (threadNovosdispositivos==null){
            return;
        }
        threadNovosdispositivos.interrupt();
        threadNovosdispositivos=null;
        synchronized (dispositivos){
            dispositivos =new ArrayList<>();
            try {
                if (welcomeSocket!=null && !welcomeSocket.isClosed())
                    welcomeSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void notificadispositivo(double d, Dispositivo dispositivo){
        try {
            Socket clientSocket = new Socket(dispositivo.ip, 5678);
            Mensagem msg = new Mensagem();
            msg.tipo="notificação de choro";
            msg.parametros.put("volume",(int)d);
            Mensagem.escrever(msg,clientSocket);
            clientSocket.close();
            Log.d("TAG-joao-ServicoCrianca","Notifiquei o pai "+ dispositivo.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG-joao-Erro:","IP= "+ dispositivo.ip);
        }

    }



}
