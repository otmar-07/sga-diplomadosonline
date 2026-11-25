package com.diplomadosonline.model;

// Profesor hereda de Persona
public class Profesor extends Persona {
    private String especialidad;
    private String materia;

    public Profesor(String cedula, String nombre, String correo, String especialidad, String materia) {
        super(cedula, nombre, correo); // Llama al constructor de Persona
        this.especialidad = especialidad;
        this.materia = materia;
    }

    // Metodos para obtener y modificar sus datos
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
}