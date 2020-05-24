package com.example.carolshaw.objetos;

import java.io.Serializable;

public class Amigo implements Serializable {



    private Integer id;
    private String nick;
    private String nombre;
    private String apellidos;
    private String avatar;
    private String ultimaCancion;
    private String artistaUltimaCancion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUltimaCancion() {
        return ultimaCancion;
    }

    public void setUltimaCancion(String ultimaCancion) {
        this.ultimaCancion = ultimaCancion;
    }

    public String getArtistaUltimaCancion() {
        return artistaUltimaCancion;
    }

    public void setArtistaUltimaCancion(String artistaUltimaCancion) {
        this.artistaUltimaCancion = artistaUltimaCancion;
    }
}
