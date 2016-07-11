package com.example.joao.baba1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedHashMap;

/**
 * Created by Joao on 06/07/2016.
 */
public class Mensagem implements Serializable{

    /*
    Tipos possíveis:
    "requisição de dispositivo": Um novo dispositivo avisando ao aparelho da criança que quer ouvi-lo. Precisa passar um objeto Dispositivo
    "notificação de choro": Celular da criança avisando que houve um choro.
    "ping": Requisição de ping, precisa responder com um pong.
    "pong": Resposta do ping
    "busca": Busca automatica, retorna seu nome.
    */

    String tipo=null;
    LinkedHashMap parametros = new LinkedHashMap();
    String portaOrigem=null;

    public static Mensagem ler(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream;
        inputStream = new ObjectInputStream(socket.getInputStream());
        Mensagem nova = (Mensagem) inputStream.readObject();
        return nova;
    }

    public static void escrever(Mensagem msg, Socket socket) throws IOException {
        ObjectOutputStream outputStream = null;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(msg);
        return;
    }

    public Mensagem(String tipo) {
        this.tipo = tipo;
    }

    public Mensagem() {
    }
}
