#include "Alumno.h"

Alumno::Alumno(string cedula, string nombre, string correo, ProgramaAcademico* programa)
    : Persona(cedula, nombre, correo) {
    this->programa = programa;
    // Las notas empiezan en cero
    this->nota1 = 0.0;
    this->nota2 = 0.0;
    this->nota3 = 0.0;
}

Alumno::~Alumno() {
    // Liberamos la memoria del programa para evitar fugas
    delete programa; 
}

double Alumno::getNota1() const { return nota1; }
void Alumno::setNota1(double nota) { this->nota1 = nota; }

double Alumno::getNota2() const { return nota2; }
void Alumno::setNota2(double nota) { this->nota2 = nota; }

double Alumno::getNota3() const { return nota3; }
void Alumno::setNota3(double nota) { this->nota3 = nota; }

ProgramaAcademico* Alumno::getPrograma() const { return programa; }

bool Alumno::estaAprobado() const {
    // Usa la regla del programa (polimorfismo)
    return programa->evaluarAprobacion(nota1, nota2, nota3);
}

string Alumno::getNombrePrograma() const {
    return programa->getNombre();
}