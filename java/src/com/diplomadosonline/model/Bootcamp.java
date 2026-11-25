package com.diplomadosonline.model;

public class Bootcamp extends ProgramaAcademico {
    
    @Override
    public boolean evaluarAprobacion(double nota1, double nota2, double nota3) {
        // Regla: ninguna nota individual puede ser menor a 14
        return (nota1 >= 14.0 && nota2 >= 14.0 && nota3 >= 14.0);
    }
}