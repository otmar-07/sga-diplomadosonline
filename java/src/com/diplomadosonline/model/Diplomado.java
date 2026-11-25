package com.diplomadosonline.model;

public class Diplomado extends ProgramaAcademico {
    
    @Override
    public boolean evaluarAprobacion(double nota1, double nota2, double nota3) {
        double promedio = (nota1 + nota2 + nota3) / 3.0;
        // Regla: promedio mayor o igual a 14
        return promedio >= 14.0;
    }
}