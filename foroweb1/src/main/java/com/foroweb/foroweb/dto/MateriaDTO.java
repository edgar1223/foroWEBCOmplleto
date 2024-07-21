package com.foroweb.foroweb.dto;

public class MateriaDTO {
    private Long id;
    private String materia;

    // Constructor
    public MateriaDTO(Long id, String materia) {
        this.id = id;
        this.materia = materia;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}
