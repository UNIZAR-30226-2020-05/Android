package com.example.carolshaw.objetos;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Artista {
    int id;
    String name;
    String image_path;
    ArrayList<String> albumes;
    ArrayList<String> canciones;

    public Artista() {
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public ArrayList<String> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(ArrayList<String> albumes) {
        this.albumes = albumes;
    }

    public ArrayList<String> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<String> canciones) {
        this.canciones = canciones;
    }
}
