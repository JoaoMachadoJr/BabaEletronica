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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceAdulto extends Service{

    //static String nome;
    static TelaAdulto tela;
    static double d;
    static Thread t;
    static String ipCrianca="172.168.1.103";
    public Socket socket=null;
    public ServerSocket socketNotificacoes=null;
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
                avisaQueEstaOuvindo();
                while (t!=null && !t.isInterrupted()){
                    try {
                        Log.d("TAG-Lucas","Serviço Adulto Rodando");
                        Socket socketTemporario = socketNotificacoes.accept();
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socketTemporario.getInputStream()));
                        final String d = inFromClient.readLine();
                        tela.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(tela,"Evento de Choro: volume "+d+"%",Toast.LENGTH_SHORT).show();
                            }
                        });
                        socketTemporario.close();
                        Thread.sleep(4000);

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

    public void avisaQueEstaOuvindo(){


        try {
            Log.d("TAG-Joao-ServicoAdulto","Vou tentar enviar minhas informações");
            socket = new Socket(ipCrianca,6789);
            socketNotificacoes=new ServerSocket(5678);
            Ouvinte eu=new Ouvinte();
            eu.ip=TelaCrianca.getIpAddress();
            eu.port=socketNotificacoes.getLocalPort();
            eu.nome="JOAO";             //meu nome
            eu.sensibilidade=90;        //Aviso que quero ser notificação sobre volumes de 90% no microfone da criança
            ObjectOutputStream outputStream=null;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(eu);
            Log.d("TAG-Joao-ServicoAdulto","Terminei de enviar minhas informações");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
