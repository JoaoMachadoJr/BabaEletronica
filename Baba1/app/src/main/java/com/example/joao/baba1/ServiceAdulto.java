package com.example.joao.baba1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServiceAdulto extends Service {

    //static String nome;
    static TelaAdulto tela;
    static Thread t;

    public static String ipCrianca = "10.0.0.103";
    public static String meuNome="";
    public static int sensibilidade=90;
    public ServerSocket socketEscuta;
    public static String meuIP=null;
    public static ArrayList<Dispositivo> descoberta_ips_descobertos= new ArrayList<>();
    public static Integer descoberta_incrementador=new Integer(0);
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
        if (t != null) {
            try {
                t.interrupt();
                t = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Inicia uma thread rodando em background
        Runnable r = new Runnable() {
            @Override
            public void run() {
                avisaQueEstaOuvindo();
                try{
                    socketEscuta = new ServerSocket(5678);
                    while (true) {
                        Log.d("TAG-joao", "adulto ouvindo porta 5678, ip local é " + TelaCrianca.getIpAddress());
                        Log.d("TAG-joao", "Chegou algo no serviço adulto");
                        Socket socket = socketEscuta.accept();
                        Mensagem m = Mensagem.ler(socket);
                        //Caso seja uma notificação de choro
                        if (m.tipo.equals("notificação de choro")) {
                            //AQUI ENTRA O CODIGO REFERENTE A INTERFACE AVISANDO DA NOTIFICACAO QUE HOUVE choro
                            trataNotificacao(m);
                        }
                        //Caso seja um ping
                        else if (m.tipo.equals("ping")) {
                            Mensagem nova = new Mensagem();
                            nova.tipo = "pong";
                            Mensagem.escrever(nova, socket);
                        } else {
                            throw new Exception("Mensagem de tipo inesperado");
                        }
                        socket.close();
                    }
                } catch (Exception e) {
                    Log.e("TAG-Lucas", "ServiceAdulto", e);
                    System.exit(1);
                }
            }
        };
        t = new Thread(r);

        t.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {    //Metodo chamado ao parar o Serviço, ele libera os recursos e mata a thread
        t.interrupt();
        //Log.e("TAG-Joao","Passou aqui: "+t.isAlive()+"   "+SoundMeter.t.isInterrupted());
        t = null;
        super.onDestroy();

    }

    public void avisaQueEstaOuvindo() {


        try {
            if (t!=null && !t.isInterrupted()){
                t.interrupt();
            }
            if (socketEscuta!=null && !socketEscuta.isClosed()){
                socketEscuta.close();
            }
            Log.d("TAG-Joao-ServicoAdulto", "Vou tentar enviar minhas informações");
            Socket socket = new Socket(ipCrianca, 6789);
            Dispositivo eu = new Dispositivo();
            eu.ip = TelaCrianca.getIpAddress();
            eu.nome = meuNome;             //meu nome
            eu.sensibilidade = sensibilidade;        //Aviso que quero ser notificação sobre volumes de 90% no microfone da criança
            Mensagem nova = new Mensagem();
            nova.tipo="requisição de dispositivo";
            nova.parametros.put("dispositivo",eu);
            Mensagem.escrever(nova, socket);
            socket.close();
            Log.d("TAG-Joao-ServicoAdulto", "Terminei de enviar minhas informações");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Trata o caso de ser uma notificação
    public void trataNotificacao(Mensagem msg){
        final int d = (int)msg.parametros.get("volume");
        Log.d("Tag-Joao","Recebi uma notificação, volume="+d);
        tela.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(tela, "Evento de Choro: volume " + d + "%", Toast.LENGTH_SHORT).show();
            }
        });
        tela.triggerNotification("Little Angel","Volume com itensidade de "+d+"% no quarto da criança",true);
    }

    public static ArrayList<Dispositivo> buscaAutomatica(){
        try{
            meuIP = TelaCrianca.getIpAddress();
            meuIP = meuIP.substring(0,meuIP.lastIndexOf('.'))+".";
            descoberta_incrementador=0;
            descoberta_ips_descobertos=new ArrayList<>();
            ArrayList<Thread> threads=new ArrayList<>();

            for (int i=0; i<=255; i++){
                final String ip=meuIP+i;
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(ip,6789),3000);
                            socket.setSoTimeout(3000);
                            Mensagem msg = new Mensagem("busca");
                            Mensagem.escrever(msg,socket);
                            Mensagem nova=Mensagem.ler(socket);
                            Dispositivo dp = new Dispositivo();
                            dp.ip=ip;
                            dp.nome=(String)nova.parametros.get("nome");
                            synchronized (descoberta_ips_descobertos){

                                descoberta_ips_descobertos.add(dp);
                            }
                            socket.close();
                        } catch (IOException e) {
                            // e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        synchronized (descoberta_incrementador){
                            descoberta_incrementador+=1;
                        }
                        Thread.currentThread().interrupt();
                    }
                };
                Thread t = new Thread(r);
                threads.add(t);
                t.start();
            }
            while (true){
                boolean terminou=false;
                synchronized (descoberta_incrementador){
                    terminou=descoberta_incrementador==256 || threads.size()==0;
                }
                if (!terminou){
                    try {
                        String saida="";
                        for (int i=0; i<threads.size(); i++){


                            if (!threads.get(i).isAlive()){
                                threads.remove(i);

                            } else{
                                saida+=threads.get(i).getId()+" ";
                            }
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Log.d("tag-joao","Terminou a busca, achou: "+descoberta_ips_descobertos.size()+" ips");
                    return descoberta_ips_descobertos;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Dispositivo>();
        }
    }
}
