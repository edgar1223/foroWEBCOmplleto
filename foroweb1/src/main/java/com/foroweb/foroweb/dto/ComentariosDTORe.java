package com.foroweb.foroweb.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ComentariosDTORe {
    private Long id;
    private String titulo;
    private String contenido;
    private Date fecha;
    private String img;
    private String archivo;
    private UsuarioDTO usuario;
    private PostDTO post;



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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public static class UsuarioDTO {
        private String nombre;
        private String apellido;
        private String email;
        private String imgUrl;

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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class PostDTO {
        private Long id;
        private String titulo;
        private String contenido;
        private String img;
        private String archivo;
        private PostResponseDTO.UsuarioDTO usuario;
        private PostResponseDTO.MateriaDTO materia;
        private Date fecha;

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public PostResponseDTO.MateriaDTO getMateria() {
            return materia;
        }

        public void setMateria(PostResponseDTO.MateriaDTO materia) {
            this.materia = materia;
        }

        public PostResponseDTO.UsuarioDTO getUsuario() {
            return usuario;
        }

        public void setUsuario(PostResponseDTO.UsuarioDTO usuario) {
            this.usuario = usuario;
        }

        public String getArchivo() {
            return archivo;
        }

        public void setArchivo(String archivo) {
            this.archivo = archivo;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        // Getters y setters
    }
}
