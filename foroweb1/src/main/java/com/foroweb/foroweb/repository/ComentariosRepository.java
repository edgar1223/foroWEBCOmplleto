package com.foroweb.foroweb.repository;

import com.foroweb.foroweb.dto.ComentariosDTO;
import com.foroweb.foroweb.dto.ComentariosDTORe;
import com.foroweb.foroweb.model.Comentarios;
import com.foroweb.foroweb.model.Departamento;
import com.foroweb.foroweb.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComentariosRepository extends JpaRepository<Comentarios, Long> {

    List<Comentarios> findByPostId(Long postId);

}
