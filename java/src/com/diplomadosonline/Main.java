package com.diplomadosonline;

import com.diplomadosonline.model.*;
import java.util.*;

public class Main {
    // Listas en memoria
    static List<Alumno> alumnos = new ArrayList<>();
    static List<Profesor> profesores = new ArrayList<>();
    
    // Estructuras de datos (Pila para deshacer y Cola para certificados)
    static Stack<String> pilaDeshacer = new Stack<>();
    static Queue<Alumno> colaCertificados = new LinkedList<>();
    
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Cargamos los datos de los archivos al iniciar
        alumnos = GestorArchivos.leerAlumnos();
        profesores = GestorArchivos.leerProfesores();

        int opcion = 0;
        while (opcion != 7) {
            System.out.println("\n==========================================================");
            System.out.println("SGA-DO: SISTEMA DIPLOMADOSONLINE");
            System.out.println("==========================================================");
            System.out.println("1. Registrar Alumno");
            System.out.println("2. Registrar Profesor");
            System.out.println("3. Registrar Notas a un Alumno");
            System.out.println("4. Deshacer Ultimo Registro de Nota");
            System.out.println("5. Generar Cola de Certificados");
            System.out.println("6. Mostrar Reporte General");
            System.out.println("7. Salir");
            System.out.println("==========================================================");
            System.out.print("Seleccione una opcion (1-7): ");

            try {
                // Usamos nextLine para evitar problemas con el buffer del teclado
                String entrada = scanner.nextLine();
                opcion = Integer.parseInt(entrada);

                switch (opcion) {
                    case 1: registrarAlumno(); break;
                    case 2: registrarProfesor(); break;
                    case 3: registrarNota(); break;
                    case 4: deshacerNota(); break;
                    case 5: generarCertificados(); break;
                    case 6: mostrarReporte(); break;
                    case 7: System.out.println("Saliendo del sistema de forma segura..."); break;
                    default: System.out.println("Opcion no valida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                // Esto atrapa si el usuario escribe letras en lugar de numeros
                System.out.println("Error: Ingrese un valor numerico valido.");
                opcion = 0; // Reiniciamos la opcion para que no se cierre
            }
        }
        scanner.close();
    }

    // OPCION 1
    private static void registrarAlumno() {
        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        
        // Validar que no exista
        for (Alumno a : alumnos) {
            if (a.getCedula().equalsIgnoreCase(cedula)) {
                System.out.println("Error: Ya existe un alumno con esa cedula.");
                return;
            }
        }

        System.out.print("Nombre Completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Tipo de Programa (1. Curso, 2. Diplomado, 3. Bootcamp): ");
        
        try {
            int tipo = Integer.parseInt(scanner.nextLine());
            ProgramaAcademico prog;
            if (tipo == 1) prog = new Curso();
            else if (tipo == 2) prog = new Diplomado();
            else if (tipo == 3) prog = new Bootcamp();
            else {
                System.out.println("Tipo invalido. Se asignara Curso por defecto.");
                prog = new Curso();
            }

            Alumno nuevo = new Alumno(cedula, nombre, correo, prog);
            alumnos.add(nuevo);
            GestorArchivos.guardarAlumno(nuevo); // Guardado inmediato en .txt
            System.out.println("Alumno registrado y guardado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un numero (1, 2 o 3).");
        }
    }

    // OPCION 2
    private static void registrarProfesor() {
        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        
        for (Profesor p : profesores) {
            if (p.getCedula().equalsIgnoreCase(cedula)) {
                System.out.println("Error: Ya existe un profesor con esa cedula.");
                return;
            }
        }

        System.out.print("Nombre Completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Especialidad (Ej. Python, Java): ");
        String especialidad = scanner.nextLine();
        System.out.print("Materia Asignada: ");
        String materia = scanner.nextLine();

        Profesor nuevo = new Profesor(cedula, nombre, correo, especialidad, materia);
        profesores.add(nuevo);
        GestorArchivos.guardarProfesor(nuevo);
        System.out.println("Profesor registrado y guardado exitosamente.");
    }

    // OPCION 3
    private static void registrarNota() {
        System.out.print("Ingrese la cedula del alumno: ");
        String cedula = scanner.nextLine();
        
        Alumno encontrado = null;
        for (Alumno a : alumnos) {
            if (a.getCedula().equalsIgnoreCase(cedula)) {
                encontrado = a;
                break;
            }
        }

        if (encontrado == null) {
            System.out.println("Error: Alumno no encontrado.");
            return;
        }

        System.out.println("Alumno: " + encontrado.getNombre());
        System.out.print("Ingrese la nota (1-20): ");
        
        try {
            double nota = Double.parseDouble(scanner.nextLine());
            
            // Buscamos cual nota vamos a modificar y guardamos el estado anterior en la Pila
            String estadoAnterior = "";
            if (encontrado.getNota1() == 0.0) {
                estadoAnterior = cedula + ",1," + encontrado.getNota1();
                encontrado.setNota1(nota);
            } else if (encontrado.getNota2() == 0.0) {
                estadoAnterior = cedula + ",2," + encontrado.getNota2();
                encontrado.setNota2(nota);
            } else if (encontrado.getNota3() == 0.0) {
                estadoAnterior = cedula + ",3," + encontrado.getNota3();
                encontrado.setNota3(nota);
            } else {
                System.out.println("El alumno ya tiene las 3 notas registradas. Use la opcion Deshacer para modificar.");
                return;
            }

            pilaDeshacer.push(estadoAnterior); // Guardamos en la Pila (LIFO)
            GestorArchivos.sobrescribirAlumnos(alumnos); // Actualizamos el .txt
            System.out.println("Nota registrada correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numerico valido para la nota.");
        }
    }

    // OPCION 4 (Pila - LIFO)
    private static void deshacerNota() {
        if (pilaDeshacer.isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }

        String ultimaAccion = pilaDeshacer.pop(); // Sacamos el ultimo registro (LIFO)
        String[] partes = ultimaAccion.split(",");
        String cedula = partes[0];
        int numeroNota = Integer.parseInt(partes[1]);
        double valorAnterior = Double.parseDouble(partes[2]);

        for (Alumno a : alumnos) {
            if (a.getCedula().equals(cedula)) {
                if (numeroNota == 1) a.setNota1(valorAnterior);
                else if (numeroNota == 2) a.setNota2(valorAnterior);
                else if (numeroNota == 3) a.setNota3(valorAnterior);
                break;
            }
        }

        GestorArchivos.sobrescribirAlumnos(alumnos);
        System.out.println("Ultima nota deshecha correctamente.");
    }

    // OPCION 5 (Cola - FIFO)
    private static void generarCertificados() {
        colaCertificados.clear(); // Limpiamos la cola

        // Filtramos a los aprobados usando Polimorfismo
        for (Alumno a : alumnos) {
            if (a.estaAprobado()) {
                colaCertificados.add(a);
            }
        }

        if (colaCertificados.isEmpty()) {
            System.out.println("No hay alumnos aprobados para certificar.");
            return;
        }

        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("certificados_pendientes.txt"))) {
            bw.write("==========================================");
            bw.newLine();
            bw.write("REPORTE DE CERTIFICADOS PENDIENTES");
            bw.newLine();
            bw.write("==========================================");
            bw.newLine();
            bw.write("Total de graduandos en cola: " + colaCertificados.size());
            bw.newLine();

            int contador = 1;
            // Procesamos la cola en orden FIFO
            while (!colaCertificados.isEmpty()) {
                Alumno a = colaCertificados.poll();
                double promedio = (a.getNota1() + a.getNota2() + a.getNota3()) / 3.0;
                
                bw.write(contador + ". [" + a.getCedula() + "] " + a.getNombre());
                bw.newLine();
                bw.write("- Programa: " + a.getNombrePrograma());
                bw.newLine();
                bw.write("- Promedio Final: " + String.format("%.1f", promedio));
                bw.newLine();
                bw.write("- Estatus: APROBADO");
                bw.newLine();
                contador++;
            }
            bw.write("==========================================");
            bw.newLine();
            bw.write("* Fin del reporte - Generado por SGA-DO *");
            
            System.out.println("Cola procesada. Archivo 'certificados_pendientes.txt' generado con exito.");
        } catch (java.io.IOException e) {
            System.out.println("Error al escribir el archivo de certificados: " + e.getMessage());
        }
    }

    // OPCION 6
    private static void mostrarReporte() {
        System.out.println("\n--- LISTA DE PROFESORES ---");
        if (profesores.isEmpty()) System.out.println("No hay profesores registrados.");
        for (Profesor p : profesores) {
            System.out.println("[" + p.getCedula() + "] " + p.getNombre() + " | Especialidad: " + p.getEspecialidad() + " | Materia: " + p.getMateria());
        }

        System.out.println("\n--- LISTA DE ALUMNOS ---");
        if (alumnos.isEmpty()) System.out.println("No hay alumnos registrados.");
        for (Alumno a : alumnos) {
            String estatus = a.estaAprobado() ? "APROBADO" : "REPROBADO";
            double promedio = (a.getNota1() + a.getNota2() + a.getNota3()) / 3.0;
            System.out.println("[" + a.getCedula() + "] " + a.getNombre() + " | Programa: " + a.getNombrePrograma() 
                             + " | Notas: " + a.getNota1() + ", " + a.getNota2() + ", " + a.getNota3() 
                             + " | Promedio: " + String.format("%.1f", promedio) + " | Estatus: " + estatus);
        }
    }
}