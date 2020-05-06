package com.example.carolshaw.objetos;

import java.util.ArrayList;

public class User {
    private int id;
    private String nombre;
    private String apellidos;
    private String nick;
    private String contrasena;
    private Boolean tipo_user; //false = usuario; true = admin
    private String fecha_nacimiento;
    private int id_ultima_reproduccion;
    private int minuto_ultima_reproduccion;
    private int tipo_ultima_reproduccion;
    private ArrayList<ListaCancion> lista_cancion;
    private ArrayList<Amigo> amigos;

    public User(String nombre, String apellidos, String nick, String contrasena, String fecha_nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nick = nick;
        this.contrasena = contrasena;
        this.tipo_user = false;
        this.fecha_nacimiento = fecha_nacimiento;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(Boolean tipo_user) {
        this.tipo_user = tipo_user;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
