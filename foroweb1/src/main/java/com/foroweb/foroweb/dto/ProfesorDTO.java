package com.foroweb.foroweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.*;
import java.util.Set;

public class ProfesorDTO {
    @NotBlank(message = "El campo 'id' es requerido")
    private Long id;

    @NotBlank(message = "El campo 'nombre' es requerido")
    private String nombre;

    @NotBlank(message = "El campo 'apellido' es requerido")
    private String apellido;

    @Email(regexp = "[a-zA-Z]+\\.[a-zA-Z]+@itoaxaca\\.edu\\.mx", message = "El campo 'email' debe tener el formato nombre.apellido@itoaxaca.edu.mx")
    private String email;

    @NotBlank(message = "El campo 'password' es requerido")
    private String password;

    @NotBlank(message = "El campo 'departamentoId' es requerido")
    private Long departamentoId;
    private String departamento;

    private String img;
    private List<Long> materiaIds;
    private  List<String> materias;
    public ProfesorDTO(Long id, String nombre, String apellido, String email, String password, String departamento, String img, List<String> materias) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.departamento = departamento;
        this.img = img;
        this.materias=materias;
    }

    public ProfesorDTO() {
    }

    public @NotBlank Long getId() {
        return id;
    }

    public void setId(@NotBlank Long id) {
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

    public @Email(regexp = "[a-zA-Z]+\\.[a-zA-Z]+@itoaxaca\\.edu\\.mx") String getEmail() {
        return email;
    }

    public void setEmail(@Email(regexp = "[a-zA-Z]+\\.[a-zA-Z]+@itoaxaca\\.edu\\.mx") String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(@NotBlank Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Long> getMateriaIds() {
        return materiaIds;
    }

    public void setMateriaIds(List<Long> materiaIds) {
        this.materiaIds = materiaIds;
    }

    public List<String> getMaterias() {
        return materias;
    }

    public void setMaterias(List<String> materias) {
        this.materias = materias;
    }
}
