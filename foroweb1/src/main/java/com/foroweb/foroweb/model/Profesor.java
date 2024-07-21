package com.foroweb.foroweb.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@DiscriminatorValue("PROFESOR")
public class Profesor extends Usuario {


    @ManyToMany
    @JoinTable(
            name = "profesor_materia",
            joinColumns = @JoinColumn(name = "profesor_id"),
            inverseJoinColumns = @JoinColumn(name = "materia_id")
    )

    private Set<Materia> materias;

    public Set<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
}
