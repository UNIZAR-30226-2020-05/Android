package com.example.carolshaw;

public class Usuario {
    private String nombre;
    private String apellidos;
    private String alias;
    private String pass;
    private Boolean tipo_user; //false = usuario; true = admin
    private String fecha_nacimiento;

    public Usuario(String nombre, String apellidos, String alias, String pass, String fecha_nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.alias = alias;
        this.pass = pass;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
