package com.example.carolshaw.objetos;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Date;

public class Podcast implements Serializable {
    private int id;
    private String name;
    private Date fecha_subida;
    private int duracion;
    private String artista;

    public Podcast() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFecha_subida() {
        return fecha_subida;
    }

    public void setFecha_subida(Date fecha_subida) {
        this.fecha_subida = fecha_subida;
    }

    public int getDuracion() {
        return duracion;
    }

    @SuppressLint("DefaultLocale")
    public String getDuracionMMSS(){
        return String.format("%02d:%02d", duracion / 60, duracion % 60);
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }
}
