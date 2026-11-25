package com.diplomadosonline.model;

// Clase base abstracta para los programas
public abstract class ProgramaAcademico {
    
    // Este metodo lo implementaran las clases hijas con sus propias reglas
    public abstract boolean evaluarAprobacion(double nota1, double nota2, double nota3);
    
}