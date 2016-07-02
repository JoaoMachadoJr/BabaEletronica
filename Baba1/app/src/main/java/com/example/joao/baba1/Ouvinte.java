package com.example.joao.baba1;

import java.io.Serializable;

/**
 * Created by Joao on 02/07/2016.
 */
public class Ouvinte implements Serializable{

    public String nome="";
    public String ip="";
    public int port=0;
    public int sensibilidade=100;

    public Ouvinte() {
    }

    @Override
    public String toString() {
        return "Ouvinte{" +
                "nome='" + nome + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", sensibilidade=" + sensibilidade +
                '}';
    }
    public Ouvinte clone(){
        Ouvinte r = new Ouvinte();
        r.nome=this.nome+"";
        r.ip=this.ip+"";
        r.sensibilidade=this.sensibilidade+0;
        r.port=this.port+0;
        return r;
    }
}
