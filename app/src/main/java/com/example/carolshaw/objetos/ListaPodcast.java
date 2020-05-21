package com.example.carolshaw.objetos;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaPodcast implements Serializable {
    private int id;
    private int id_usuario;
    private String nombre;
    private ArrayList<Podcast> podcasts;

    public ListaPodcast() {
    }

    public ListaPodcast(int id, int id_usuario, String nombre) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.podcasts = new ArrayList<Podcast>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}
