#ifndef PERSONA_H
#define PERSONA_H

#include <string>
using namespace std;

class Persona {
protected: // Usamos protected para que las clases hijas puedan acceder en C++
    string cedula;
    string nombre;
    string correo;

public:
    // Constructor
    Persona(string cedula, string nombre, string correo);
    
    // Destructor virtual 
    virtual ~Persona();

    // Metodos getters y setters
    string getCedula() const;
    string getNombre() const;
    string getCorreo() const;
};

#endif