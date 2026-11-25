package com.diplomadosonline.model;

// Alumno hereda de Persona
public class Alumno extends Persona {
    private double nota1;
    private double nota2;
    private double nota3;
    private ProgramaAcademico programa; // Ahora es un objeto de programa

    public Alumno(String cedula, String nombre, String correo, ProgramaAcademico programa) {
        super(cedula, nombre, correo);
        this.programa = programa;
        // Las notas empiezan en cero
        this.nota1 = 0.0;
        this.nota2 = 0.0;
        this.nota3 = 0.0;
    }

    // Metodos para obtener y modificar las notas
    public double getNota1() { return nota1; }
    public void setNota1(double nota1) { this.nota1 = nota1; }

    public double getNota2() { return nota2; }
    public void setNota2(double nota2) { this.nota2 = nota2; }

    public double getNota3() { return nota3; }
    public void setNota3(double nota3) { this.nota3 = nota3; }

    public ProgramaAcademico getPrograma() { return programa; }
    public void setPrograma(ProgramaAcademico programa) { this.programa = programa; }

    // Este metodo usa el polimorfismo para saber si aprobo o no
    public boolean estaAprobado() {
        return programa.evaluarAprobacion(nota1, nota2, nota3);
    }

    // Nos devuelve el nombre del programa (Curso, Diplomado o Bootcamp)
    public String getNombrePrograma() {
        return programa.getClass().getSimpleName();
    }
}