package com.foroweb.foroweb.model;
import jakarta.persistence.*;

import java.util.Set;
@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    @ManyToOne
    @JoinColumn(name = "institucion_id")
    private Institucion institucion;
    @OneToMany(mappedBy = "departamento")
    private Set<Usuario> usuarios;
    @ManyToMany
    @JoinTable(
            name = "departamento_materia",
            joinColumns = @JoinColumn(name = "departamento_id"),
            inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    private Set<Materia> materias;
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

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
}