#include "Persona.h"

// Implementacion del constructor
Persona::Persona(string cedula, string nombre, string correo) {
    this->cedula = cedula;
    this->nombre = nombre;
    this->correo = correo;
}

// Destructor
Persona::~Persona() {
}

// Implementacion de los getters
string Persona::getCedula() const { return cedula; }
string Persona::getNombre() const { return nombre; }
string Persona::getCorreo() const { return correo; }