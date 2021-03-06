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
    private ArrayList<ListaPodcast> lista_podcast;
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

    public void setUsuarioDto(UsuarioDto usuarioLog) {
        id = usuarioLog.getId();
        nombre = usuarioLog.getNombre();
        apellidos = usuarioLog.getApellidos();
        nick = usuarioLog.getNick();
        contrasena = usuarioLog.getContrasena();
        tipo_user = usuarioLog.getTipo_user();
        fecha_nacimiento = usuarioLog.getFecha_nacimiento();
        nombre_avatar = usuarioLog.getNombre_avatar();
        id_ultima_reproduccion = usuarioLog.getId_ultima_reproduccion();
        minuto_ultima_reproduccion = usuarioLog.getMinuto_ultima_reproduccion();
        tipo_ultima_reproduccion = usuarioLog.getTipo_ultima_reproduccion();
        lista_cancion = usuarioLog.getLista_cancion();
        amigos = usuarioLog.getAmigos();
        lista_podcast = usuarioLog.getLista_podcast();
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
    public void deleteLista_cancion(int indice) {
        this.lista_cancion.remove(indice);
    }

    public void addLista_cancion(ListaCancion lista_cancion) {
        this.lista_cancion.add(lista_cancion);
    }

    public ArrayList<Amigo> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<Amigo> amigos) {
        this.amigos = amigos;
    }

    public ArrayList<ListaPodcast> getLista_podcast() {
        return lista_podcast;
    }

    public void setLista_podcast(ArrayList<ListaPodcast> lista_podcast) {
        this.lista_podcast = lista_podcast;
    }

    public void deleteLista_podcast(int indice) {
        this.lista_podcast.remove(indice);
    }

    public void addLista_podcast(ListaPodcast lista_podcast) {
        this.lista_podcast.add(lista_podcast);
    }
}
