package com.example.carolshaw.objetos;

import android.annotation.SuppressLint;

import java.util.ArrayList;

/* Clase igual que Cancion pero con Arraylist de String para artistas que solo se usa en ListaCancionUsuarioDto
 */
public class CancionUsuarioDto {
    private int id;
    private String name;
    private String fecha_subida;
    private int duracion; //En segundos
    private String album;
    private ArrayList<String> artistas;

    public CancionUsuarioDto(){}

    public CancionUsuarioDto(int id, String name, String fecha_subida, int duracion, String album, ArrayList<String> artistas) {
        this.id = id;
        this.name = name;
        this.fecha_subida = fecha_subida;
        this.duracion = duracion;
        this.album = album;
        this.artistas = artistas;
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

    public String getFecha_subida() {
        return fecha_subida;
    }

    public void setFecha_subida(String fecha_subida) {
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getArtistas() {
        return artistas;
    }

    public void setArtistas(ArrayList<String> artistas) {
        this.artistas = artistas;
    }
}
