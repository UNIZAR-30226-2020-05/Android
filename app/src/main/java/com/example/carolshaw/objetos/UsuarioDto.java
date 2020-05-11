package com.example.carolshaw.objetos;

import android.app.Application;

import java.util.ArrayList;

public class UsuarioDto extends Application {
    private int id;
    private String nombre;
    private String apellidos;
    private String nick;
    private String contrasena;
    private Boolean tipo_user; //false = usuario; true = admin
    private String fecha_nacimiento;
    private String nombre_avatar;
    private int id_ultima_reproduccion;
    private int minuto_ultima_reproduccion;
    private int tipo_ultima_reproduccion;
    private ArrayList<ListaCancion> lista_cancion;
    private ArrayList<Amigo> amigos;

    public UsuarioDto(){
        //Constructor por defecto
    }
    public UsuarioDto(String nombre, String apellidos, String nick, String contrasena, String fecha_nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nick = nick;
        this.contrasena = contrasena;
        this.tipo_user = false;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNombre_avatar() {
        return nombre_avatar;
    }

    public void setNombre_avatar(String nombre_avatar) {
        this.nombre_avatar = nombre_avatar;
    }

    public int getId_ultima_reproduccion() {
        return id_ultima_reproduccion;
    }

    public void setId_ultima_reproduccion(int id_ultima_reproduccion) {
        this.id_ultima_reproduccion = id_ultima_reproduccion;
    }

    public int getMinuto_ultima_reproduccion() {
        return minuto_ultima_reproduccion;
    }

    public void setMinuto_ultima_reproduccion(int minuto_ultima_reproduccion) {
        this.minuto_ultima_reproduccion = minuto_ultima_reproduccion;
    }

    public int getTipo_ultima_reproduccion() {
        return tipo_ultima_reproduccion;
    }

    public void setTipo_ultima_reproduccion(int tipo_ultima_reproduccion) {
        this.tipo_ultima_reproduccion = tipo_ultima_reproduccion;
    }

    public ArrayList<ListaCancion> getLista_cancion() {
        return lista_cancion;
    }

    public void setLista_cancion(ArrayList<ListaCancion> lista_cancion) {
        this.lista_cancion = lista_cancion;
    }

    public ArrayList<Amigo> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<Amigo> amigos) {
        this.amigos = amigos;
    }
}
