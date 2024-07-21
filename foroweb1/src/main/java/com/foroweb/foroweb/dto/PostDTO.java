
package com.foroweb.foroweb.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class PostDTO {
    private Long usuarioId;
    private Long materiaId;
    private String titulo;
    private String contenido;
    private MultipartFile img;
    private MultipartFile archivo;
    private  Long id;
    private Date fecha;

public PostDTO(){}
    public PostDTO(Long id, Long materiaId, String titulo, String contenido) {
        this.id = id;
        this.materiaId = materiaId;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public MultipartFile getArchivo() {
        return archivo;
    }

    public void setArchivo(MultipartFile archivo) {
        this.archivo = archivo;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
