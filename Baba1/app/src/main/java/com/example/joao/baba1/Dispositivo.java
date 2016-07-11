package com.example.joao.baba1;

import java.io.Serializable;

/**
 * Created by Joao on 02/07/2016.
 */
public class Dispositivo implements Serializable{

    public String nome="";
    public String ip="";
    public int sensibilidade=100;

    public Dispositivo() {
    }

    @Override
    public String toString() {
        return "Dispositivo{" +
                "nome='" + nome + '\'' +
                ", ip='" + ip + '\'' +
                ", sensibilidade=" + sensibilidade +
                '}';
    }
    public Dispositivo clone(){
        Dispositivo r = new Dispositivo();
        r.nome=this.nome+"";
        r.ip=this.ip+"";
        r.sensibilidade=this.sensibilidade+0;
        return r;
    }
}
