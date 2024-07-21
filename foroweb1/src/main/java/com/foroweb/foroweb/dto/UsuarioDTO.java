package com.foroweb.foroweb.dto;

import org.springframework.web.multipart.MultipartFile;

public class UsuarioDTO {
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private MultipartFile img;
    private Long departamentoId;
    private String semestre;
    private String tipoUsuario;
    public UsuarioDTO( String nombre, String apellido, String email, MultipartFile img, Long departamentoId, String semestre) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.img = img;
        this.departamentoId = departamentoId;
        this.semestre = semestre;
    }

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
