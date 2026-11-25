# SGA-DO - Implementación en Java

Sistema de Gestión Académica (SGA-DO) desarrollado por consola en Java. Guarda la información en archivos de texto plano (.txt).

## Cómo ejecutar el proyecto

Este proyecto no usa Maven ni Gradle, por lo que se ejecuta directamente desde la terminal. 

Abre una terminal en la carpeta raíz del proyecto y ejecuta los siguientes comandos en orden:

```bash
cd java/src
javac com/diplomadosonline/Main.java com/diplomadosonline/model/*.java
java com.diplomadosonline.Main
```

## Estructura del Proyecto

El código fuente está organizado de la siguiente manera:

java/
└── src/
    └── com/
        └── diplomadosonline/
            ├── Main.java
            └── model/
                ├── Persona.java
                ├── Alumno.java
                ├── Profesor.java
                ├── ProgramaAcademico.java
                ├── Curso.java
                ├── Diplomado.java
                ├── Bootcamp.java
                └── GestorArchivos.java
