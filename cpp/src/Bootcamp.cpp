#include "Bootcamp.h"

bool Bootcamp::evaluarAprobacion(double n1, double n2, double n3) const {
    // Regla: ninguna nota individual puede ser menor a 14
    return (n1 >= 14.0 && n2 >= 14.0 && n3 >= 14.0);
}
string Bootcamp::getNombre() const { return "Bootcamp"; }
