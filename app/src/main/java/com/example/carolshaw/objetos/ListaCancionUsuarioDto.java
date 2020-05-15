package com.example.carolshaw.objetos;

import java.util.ArrayList;

/* Clase igual que Cancion pero con CancionUsuarioDto que solo se usa en UsuarioDto
 */
public class ListaCancionUsuarioDto {
    int id;
    int id_usuario;
    String nombre;
    ArrayList<CancionUsuarioDto> canciones;

    public ListaCancionUsuarioDto() {
    }

    public ListaCancionUsuarioDto(int id, int id_usuario, String nombre) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.canciones = new ArrayList<CancionUsuarioDto>();
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

    public ArrayList<CancionUsuarioDto> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<CancionUsuarioDto> canciones) {
        this.canciones = canciones;
    }

    public void addCancion(CancionUsuarioDto cancion){
        this.canciones.add(cancion);
    }
}
