#include "Profesor.h"

// Constructor: pasamos los datos basicos a Persona y guardamos los especificos
Profesor::Profesor(string cedula, string nombre, string correo, string especialidad, string materia)
    : Persona(cedula, nombre, correo) { 
    this->especialidad = especialidad;
    this->materia = materia;
}

// Destructor (vacio porque aqui no hay memoria dinamica)
Profesor::~Profesor() {
}

// Metodos para obtener los datos
string Profesor::getEspecialidad() const { return especialidad; }
string Profesor::getMateria() const { return materia; }