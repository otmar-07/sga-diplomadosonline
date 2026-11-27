#ifndef ALUMNO_H
#define ALUMNO_H

#include "Persona.h"
#include "ProgramaAcademico.h"

class Alumno : public Persona {
private:
    double nota1, nota2, nota3;
    ProgramaAcademico* programa; // Puntero al tipo de programa (Curso, Diplomado, etc.)

public:
    Alumno(string cedula, string nombre, string correo, ProgramaAcademico* programa);
    virtual ~Alumno(); // Destructor

    // Metodos para obtener y modificar notas
    double getNota1() const;
    void setNota1(double nota);
    double getNota2() const;
    void setNota2(double nota);
    double getNota3() const;
    void setNota3(double nota);

    ProgramaAcademico* getPrograma() const;
    
    // Revisa si aprobo usando el programa asignado
    bool estaAprobado() const;
    string getNombrePrograma() const;
};

#endif