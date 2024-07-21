package com.foroweb.foroweb.dto;


import com.foroweb.foroweb.model.Institucion;

public class DepartamentoDTO {

    private Long id;
    private String nombre;
    private Institucion institucion;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }
}
