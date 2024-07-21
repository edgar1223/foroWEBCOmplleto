package com.foroweb.foroweb.dto;

public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String imgUrl; // Cambiar MultipartFile a String
    private Long departamentoId;
    private String semestre;
    private DepartamentoDTO departamento;
    private int notificaciones;
    public UsuarioResponseDTO(Long id,String nombre, String email, String apellido, String imgUrl, int notificaciones,Long departamentoId, String semestre) {
        this.id=id;
        this.nombre = nombre;
        this.email = apellido;
        this.apellido = email;
        this.imgUrl = imgUrl;
        this.notificaciones=notificaciones;
        this.departamentoId = departamentoId;
        this.semestre = semestre;

    }

    public UsuarioResponseDTO() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    public int getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(int notificaciones) {
        this.notificaciones = notificaciones;
    }

    public static class DepartamentoDTO {
        private Long id;
        private String nombre;

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

        // Getters y Setters
    }
}
