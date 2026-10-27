import os
from collections import deque


# ============================================================
# JERARQUÍA DE PERSONAS
# ============================================================

class Persona:
    def __init__(self, cedula, nombre, correo):
        self.__cedula = cedula
        self.__nombre = nombre
        self.__correo = correo

    def get_cedula(self):
        return self.__cedula

    def get_nombre(self):
        return self.__nombre

    def get_correo(self):
        return self.__correo

    def set_nombre(self, nombre):
        self.__nombre = nombre

    def set_correo(self, correo):
        self.__correo = correo


class Alumno(Persona):
    def __init__(self, cedula, nombre, correo, programa):
        super().__init__(cedula, nombre, correo)
        self.__notas = [0.0, 0.0, 0.0]
        self.__programa = programa

    def get_notas(self):
        return self.__notas[:]

    def get_programa(self):
        return self.__programa

    def asignar_nota(self, indice, nota):
        if 0 <= indice < 3:
            self.__notas[indice] = nota

    def obtener_nota(self, indice):
        if 0 <= indice < 3:
            return self.__notas[indice]
        return None

    def promedio(self):
        return sum(self.__notas) / len(self.__notas)

    def esta_aprobado(self):
        return self.__programa.calcular_aprobacion(self.__notas)


class Profesor(Persona):
    def __init__(self, cedula, nombre, correo, especialidad, materia):
        super().__init__(cedula, nombre, correo)
        self.__especialidad = especialidad
        self.__materia = materia

    def get_especialidad(self):
        return self.__especialidad

    def get_materia(self):
        return self.__materia

    def set_materia(self, materia):
        self.__materia = materia


# ============================================================
# JERARQUÍA DE PROGRAMAS ACADÉMICOS
# ============================================================

class ProgramaAcademico:
    def calcular_aprobacion(self, notas):
        raise NotImplementedError("Debe implementarse en la subclase")


class Curso(ProgramaAcademico):
    def calcular_aprobacion(self, notas):
        promedio = sum(notas) / len(notas)
        return promedio >= 10


class Diplomado(ProgramaAcademico):
    def calcular_aprobacion(self, notas):
        promedio = sum(notas) / len(notas)
        return promedio >= 14


class Bootcamp(ProgramaAcademico):
    def calcular_aprobacion(self, notas):
        return all(nota >= 14 for nota in notas)


def crear_programa(tipo):
    tipo = tipo.strip().lower()
    if tipo == "curso":
        return Curso()
    elif tipo == "diplomado":
        return Diplomado()
    elif tipo == "bootcamp":
        return Bootcamp()
    return None


# ============================================================
# CLASE PRINCIPAL: SISTEMA DE GESTIÓN
# ============================================================

class SistemaGestion:
    def __init__(self):
        self.__alumnos = []
        self.__profesores = []
        self.__pila_notas = []
        self.cargar_datos()

    # ---------- Persistencia ----------
    def cargar_datos(self):
        self.__alumnos = self.__cargar_alumnos()
        self.__profesores = self.__cargar_profesores()

    def __cargar_alumnos(self):
        alumnos = []
        if not os.path.exists("alumnos.txt"):
            return alumnos
        with open("alumnos.txt", "r", encoding="utf-8") as f:
            for linea in f:
                linea = linea.strip()
                if not linea:
                    continue
                partes = [p.strip() for p in linea.split(",")]
                if len(partes) != 7:
                    continue
                cedula, nombre, correo, tipo, n1, n2, n3 = partes
                programa = crear_programa(tipo)
                if programa is None:
                    continue
                alumno = Alumno(cedula, nombre, correo, programa)
                try:
                    alumno.asignar_nota(0, float(n1))
                    alumno.asignar_nota(1, float(n2))
                    alumno.asignar_nota(2, float(n3))
                except ValueError:
                    pass
                alumnos.append(alumno)
        return alumnos

    def __cargar_profesores(self):
        profesores = []
        if not os.path.exists("profesores.txt"):
            return profesores
        with open("profesores.txt", "r", encoding="utf-8") as f:
            for linea in f:
                linea = linea.strip()
                if not linea:
                    continue
                partes = [p.strip() for p in linea.split(",")]
                if len(partes) != 5:
                    continue
                cedula, nombre, correo, especialidad, materia = partes
                profesores.append(Profesor(cedula, nombre, correo, especialidad, materia))
        return profesores

    def guardar_alumnos(self):
        with open("alumnos.txt", "w", encoding="utf-8") as f:
            for alumno in self.__alumnos:
                notas = alumno.get_notas()
                tipo = type(alumno.get_programa()).__name__
                f.write(f"{alumno.get_cedula()},{alumno.get_nombre()},{alumno.get_correo()},{tipo},{notas[0]},{notas[1]},{notas[2]}\n")

    def guardar_profesores(self):
        with open("profesores.txt", "w", encoding="utf-8") as f:
            for p in self.__profesores:
                f.write(f"{p.get_cedula()},{p.get_nombre()},{p.get_correo()},{p.get_especialidad()},{p.get_materia()}\n")

    # ---------- Operaciones ----------
    def registrar_alumno(self, cedula, nombre, correo, tipo):
        for a in self.__alumnos:
            if a.get_cedula() == cedula:
                return False, f"Ya existe un alumno con la cédula {cedula}."
        programa = crear_programa(tipo)
        if programa is None:
            return False, "Tipo de programa inválido. Use Curso, Diplomado o Bootcamp."
        alumno = Alumno(cedula, nombre, correo, programa)
        self.__alumnos.append(alumno)
        self.guardar_alumnos()
        return True, f"Alumno {nombre} registrado exitosamente."

    def registrar_profesor(self, cedula, nombre, correo, especialidad, materia):
        for p in self.__profesores:
            if p.get_cedula() == cedula:
                return False, f"Ya existe un profesor con la cédula {cedula}."
        profesor = Profesor(cedula, nombre, correo, especialidad, materia)
        self.__profesores.append(profesor)
        self.guardar_profesores()
        return True, f"Profesor {nombre} registrado exitosamente."

    def buscar_alumno(self, cedula):
        for a in self.__alumnos:
            if a.get_cedula() == cedula:
                return a
        return None

    def registrar_nota(self, cedula, num_nota, valor):
        alumno = self.buscar_alumno(cedula)
        if alumno is None:
            return False, f"No se encontró alumno con cédula {cedula}."
        if num_nota < 1 or num_nota > 3:
            return False, "El número de nota debe estar entre 1 y 3."
        if valor < 0 or valor > 20:
            return False, "La nota debe estar entre 0 y 20."
        indice = num_nota - 1
        anterior = alumno.obtener_nota(indice)
        alumno.asignar_nota(indice, valor)
        self.__pila_notas.append((cedula, indice, anterior))
        self.guardar_alumnos()
        return True, f"Nota {num_nota} actualizada a {valor} para {alumno.get_nombre()}."

    def deshacer_nota(self):
        if not self.__pila_notas:
            return False, "No hay notas para deshacer."
        cedula, indice, anterior = self.__pila_notas.pop()
        alumno = self.buscar_alumno(cedula)
        if alumno is None:
            return False, "No se encontró el alumno."
        alumno.asignar_nota(indice, anterior)
        self.guardar_alumnos()
        return True, f"Nota deshecha. Se restauró el valor {anterior} para {alumno.get_nombre()}."

    def generar_certificados(self):
        cola = deque()
        for alumno in self.__alumnos:
            if alumno.esta_aprobado():
                cola.append(alumno)
        total = len(cola)
        with open("certificados_pendientes.txt", "w", encoding="utf-8") as f:
            f.write("=========================================\n")
            f.write("REPORTE DE CERTIFICADOS PENDIENTES\n")
            f.write("=========================================\n")
            f.write(f"Total de graduandos en cola: {total}\n\n")
            contador = 1
            while cola:
                alumno = cola.popleft()
                f.write(f"{contador}. [{alumno.get_cedula()}] {alumno.get_nombre()}\n")
                f.write(f"   - Programa: {type(alumno.get_programa()).__name__}\n")
                f.write(f"   - Promedio Final: {alumno.promedio():.1f}\n")
                if isinstance(alumno.get_programa(), Bootcamp):
                    estatus = "APROBADO (Cumple regla de ninguna nota < 14)"
                else:
                    estatus = "APROBADO"
                f.write(f"   - Estatus: {estatus}\n\n")
                contador += 1
            f.write("* Fin del reporte - Generado por SGA-DO *\n")
        return True, f"Reporte generado. Total de graduandos: {total}."

    def mostrar_reporte(self):
        lineas = []
        lineas.append("PROFESORES:")
        if not self.__profesores:
            lineas.append("  (No hay profesores registrados)")
        for p in self.__profesores:
            lineas.append(f"  [{p.get_cedula()}] {p.get_nombre()} - Especialidad: {p.get_especialidad()} - Materia: {p.get_materia()}")
        lineas.append("")
        lineas.append("ALUMNOS:")
        if not self.__alumnos:
            lineas.append("  (No hay alumnos registrados)")
        for a in self.__alumnos:
            estado = "APROBADO" if a.esta_aprobado() else "REPROBADO"
            tipo = type(a.get_programa()).__name__
            lineas.append(f"  [{a.get_cedula()}] {a.get_nombre()} - Programa: {tipo} - Notas: {a.get_notas()} - Promedio: {a.promedio():.1f} - Estatus: {estado}")
        return "\n".join(lineas)

    def guardar_todo(self):
        self.guardar_alumnos()
        self.guardar_profesores()


# ============================================================
# MENÚ POR CONSOLA
# ============================================================

def main():
    sistema = SistemaGestion()

    while True:
        print("==================================================")
        print("SGA-DO: SISTEMA DIPLOMADOSONLINE")
        print("==================================================")
        print("1. Registrar Alumno")
        print("2. Registrar Profesor")
        print("3. Registrar Notas a un Alumno")
        print("4. Deshacer Último Registro de Nota")
        print("5. Generar Cola de Certificados")
        print("6. Mostrar Reporte General")
        print("7. Salir")
        print("==================================================")

        try:
            opcion = input("Seleccione una opción (1-7): ").strip()
        except (KeyboardInterrupt, EOFError):
            print("\nSaliendo...")
            break

        if opcion == "1":
            print("\n--- REGISTRAR ALUMNO ---")
            cedula = input("Cédula: ").strip()
            nombre = input("Nombre completo: ").strip()
            correo = input("Correo: ").strip()
            tipo = input("Tipo de programa (Curso/Diplomado/Bootcamp): ").strip()
            ok, msg = sistema.registrar_alumno(cedula, nombre, correo, tipo)
            print(msg + "\n")

        elif opcion == "2":
            print("\n--- REGISTRAR PROFESOR ---")
            cedula = input("Cédula: ").strip()
            nombre = input("Nombre completo: ").strip()
            correo = input("Correo: ").strip()
            especialidad = input("Especialidad: ").strip()
            materia = input("Materia asignada: ").strip()
            ok, msg = sistema.registrar_profesor(cedula, nombre, correo, especialidad, materia)
            print(msg + "\n")

        elif opcion == "3":
            print("\n--- REGISTRAR NOTA ---")
            cedula = input("Cédula del alumno: ").strip()
            try:
                num_nota = int(input("Número de nota (1-3): ").strip())
                valor = float(input("Valor de la nota (0-20): ").strip())
            except ValueError:
                print("Error: Ingrese valores numéricos válidos.\n")
                continue
            ok, msg = sistema.registrar_nota(cedula, num_nota, valor)
            print(msg + "\n")

        elif opcion == "4":
            print("\n--- DESHACER ÚLTIMA NOTA ---")
            ok, msg = sistema.deshacer_nota()
            print(msg + "\n")

        elif opcion == "5":
            print("\n--- GENERAR COLA DE CERTIFICADOS ---")
            ok, msg = sistema.generar_certificados()
            print(msg + "\n")

        elif opcion == "6":
            print("\n--- REPORTE GENERAL ---")
            print(sistema.mostrar_reporte())
            print()

        elif opcion == "7":
            print("Guardando cambios y saliendo...")
            sistema.guardar_todo()
            break

        else:
            print("Error: Opción inválida. Ingrese un número del 1 al 7.\n")


if __name__ == "__main__":
    main()