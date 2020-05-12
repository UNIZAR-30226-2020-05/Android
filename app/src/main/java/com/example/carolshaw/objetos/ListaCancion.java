package com.example.carolshaw.objetos;

import java.util.ArrayList;

public class ListaCancion {

    int id;
    int id_usuario;
    String nombre;
    ArrayList<Cancion> canciones;

    public ListaCancion() {
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

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public void addCancion(Cancion cancion){
        this.canciones.add(cancion);
    }
}
