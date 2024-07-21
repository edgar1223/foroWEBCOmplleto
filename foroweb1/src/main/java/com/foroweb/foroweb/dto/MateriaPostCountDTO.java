package com.foroweb.foroweb.dto;

public class MateriaPostCountDTO {
    private Long materiaId;
    private String materiaNombre;
    private Long postCount;

    // Constructor, getters y setters
    public MateriaPostCountDTO(Long materiaId, String materiaNombre, Long postCount) {
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.postCount = postCount;
    }

    public Long getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }

    public String getMateriaNombre() {
        return materiaNombre;
    }

    public void setMateriaNombre(String materiaNombre) {
        this.materiaNombre = materiaNombre;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }
}
