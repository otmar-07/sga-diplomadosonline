package com.diplomadosonline.model;

public class Persona {
    // Datos basicos de la persona
    private String cedula;
    private String nombre;
    private String correo;

    // Constructor
    public Persona(String cedula, String nombre, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
    }

    // Metodos para obtener y modificar los datos
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}