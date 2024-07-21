package com.foroweb.foroweb.dto;

import com.foroweb.foroweb.model.Post;
import com.foroweb.foroweb.model.Usuario;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ComentariosDTO {

    private Long id;
    private String titulo;
    private String contenido;
    private MultipartFile img;
    private MultipartFile archivo;
    private Date fecha;
    private Long post;

    public ComentariosDTO(Long id, String titulo, String contenido, MultipartFile img, MultipartFile archivo, Date fecha, Long post) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.img = img;
        this.archivo = archivo;
        this.fecha = fecha;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public MultipartFile getArchivo() {
        return archivo;
    }

    public void setArchivo(MultipartFile archivo) {
        this.archivo = archivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }



    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }
}
