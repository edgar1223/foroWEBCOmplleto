package com.foroweb.foroweb.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materia;
    private String fdescrip;

    private int semestre;
    @ManyToMany(mappedBy = "materias")
    private Set<Profesor> profesores;

    @OneToMany(mappedBy = "materia")
    private Set<Post> posts;

    @ManyToMany(mappedBy = "materias")
    private Set<Departamento> departamentos;

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

    public String getFdescrip() {
        return fdescrip;
    }

    public void setFdescrip(String fdescrip) {
        this.fdescrip = fdescrip;
    }


    public Set<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(Set<Profesor> profesores) {
        this.profesores = profesores;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Set<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
}
