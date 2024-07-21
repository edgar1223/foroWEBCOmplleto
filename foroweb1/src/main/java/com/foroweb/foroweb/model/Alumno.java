package com.foroweb.foroweb.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {

    private String Semestre;

    public String getSemestre() {
        return Semestre;
    }

    public void setSemestre(String semestre) {
        Semestre = semestre;
    }
}

