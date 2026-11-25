package com.diplomadosonline.model;

public class Curso extends ProgramaAcademico {
    
    @Override
    public boolean evaluarAprobacion(double nota1, double nota2, double nota3) {
        double promedio = (nota1 + nota2 + nota3) / 3.0;
        // Regla: promedio mayor o igual a 10
        return promedio >= 10.0;
    }
}