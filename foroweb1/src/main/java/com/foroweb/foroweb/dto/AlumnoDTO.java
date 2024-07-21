package com.foroweb.foroweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AlumnoDTO {
    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "El numero de control debe ser un número de 8 dígitos")
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email(regexp = "\\d{8}@itoaxaca\\.edu\\.mx", message = "El numero de control debe seguir el formato 'id@itoaxaca.edu.mx'")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String semestre;

    @NotBlank
    private  Long departamento_id;

    public @NotBlank @Pattern(regexp = "\\d{8}", message = "El numero de control debe ser un número de 8 dígitos") Long getId() {
        return id;
    }

    public void setId(@NotBlank @Pattern(regexp = "\\d{8}", message = "El numero de control debe ser un número de 8 dígitos") Long id) {
        this.id = id;
    }

    public @NotBlank String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank String apellido) {
        this.apellido = apellido;
    }

    public @Email(regexp = "\\d{8}@itoaxaca\\.edu\\.mx", message = "El numero de control debe seguir el formato 'id@itoaxaca.edu.mx'") String getEmail() {
        return email;
    }

    public void setEmail(@Email(regexp = "\\d{8}@itoaxaca\\.edu\\.mx", message = "El numero de control debe seguir el formato 'id@itoaxaca.edu.mx'") String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank String getSemestre() {
        return semestre;
    }

    public void setSemestre(@NotBlank String semestre) {
        this.semestre = semestre;
    }

    public @NotBlank Long getDepartamento_id() {
        return departamento_id;
    }

    public void setDepartamento_id(@NotBlank Long departamento_id) {
        this.departamento_id = departamento_id;
    }
}