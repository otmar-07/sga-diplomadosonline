package com.diplomadosonline.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    
    // Guarda un alumno nuevo al final del archivo
    public static void guardarAlumno(Alumno alumno) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("alumnos.txt", true), StandardCharsets.UTF_8))) {
            // Formato: Cedula, Nombre, Correo, TipoPrograma, Nota1, Nota2, Nota3
            String linea = alumno.getCedula() + "," + alumno.getNombre() + "," + alumno.getCorreo() + "," 
                         + alumno.getNombrePrograma() + "," + alumno.getNota1() + "," 
                         + alumno.getNota2() + "," + alumno.getNota3();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar alumno: " + e.getMessage());
        }
    }

    // Guarda un profesor nuevo al final del archivo
    public static void guardarProfesor(Profesor profesor) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("profesores.txt", true), StandardCharsets.UTF_8))) {
            // Formato: Cedula, Nombre, Correo, Especialidad, Materia
            String linea = profesor.getCedula() + "," + profesor.getNombre() + "," + profesor.getCorreo() + "," 
                         + profesor.getEspecialidad() + "," + profesor.getMateria();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar profesor: " + e.getMessage());
        }
    }

    // Lee todos los alumnos del archivo y los devuelve en una lista
    public static List<Alumno> leerAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        File archivo = new File("alumnos.txt");
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Verificamos que la linea tenga los 7 datos esperados
                if (datos.length == 7) {
                    // Creamos el programa segun el tipo guardado
                    ProgramaAcademico prog;
                    if (datos[3].equals("Curso")) prog = new Curso();
                    else if (datos[3].equals("Diplomado")) prog = new Diplomado();
                    else prog = new Bootcamp();

                    Alumno a = new Alumno(datos[0], datos[1], datos[2], prog);
                    a.setNota1(Double.parseDouble(datos[4]));
                    a.setNota2(Double.parseDouble(datos[5]));
                    a.setNota3(Double.parseDouble(datos[6]));
                    lista.add(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer alumnos: " + e.getMessage());
        }
        return lista;
    }

    // Lee todos los profesores del archivo
    public static List<Profesor> leerProfesores() {
        List<Profesor> lista = new ArrayList<>();
        File archivo = new File("profesores.txt");
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Verificamos que la linea tenga los 5 datos esperados
                if (datos.length == 5) {
                    lista.add(new Profesor(datos[0], datos[1], datos[2], datos[3], datos[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer profesores: " + e.getMessage());
        }
        return lista;
    }

    // Actualiza el archivo de alumnos (util cuando cambiamos notas o usamos Deshacer)
    public static void sobrescribirAlumnos(List<Alumno> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("alumnos.txt", false), StandardCharsets.UTF_8))) {
            for (Alumno a : lista) {
                String linea = a.getCedula() + "," + a.getNombre() + "," + a.getCorreo() + "," 
                             + a.getNombrePrograma() + "," + a.getNota1() + "," 
                             + a.getNota2() + "," + a.getNota3();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar alumnos: " + e.getMessage());
        }
    }
    
    // Actualiza el archivo de profesores
    public static void sobrescribirProfesores(List<Profesor> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("profesores.txt", false), StandardCharsets.UTF_8))) {
            for (Profesor p : lista) {
                String linea = p.getCedula() + "," + p.getNombre() + "," + p.getCorreo() + "," 
                             + p.getEspecialidad() + "," + p.getMateria();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar profesores: " + e.getMessage());
        }
    }
}