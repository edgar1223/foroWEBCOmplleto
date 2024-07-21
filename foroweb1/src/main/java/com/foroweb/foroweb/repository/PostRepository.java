package com.foroweb.foroweb.repository;


import com.foroweb.foroweb.model.Materia;
import com.foroweb.foroweb.model.Post;
import com.foroweb.foroweb.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Query("FROM Post WHERE id = :id")
    List<Post> findIdPost(@Param("id") Long id);

    @Query("FROM Post")
    List<Post> findAllPost();


    @Query("SELECT p FROM Post p WHERE p.usuario.id = :usuario_id ORDER BY p.fecha DESC")
    List<Post> findUsuariIdPost(@Param("usuario_id") Long usuario_id);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.vista = :vista  WHERE p.id = :id")
    void updateVista(@Param("id") Long id,@Param("vista") boolean vista);



    @Query("SELECT p.materia.id, p.materia.materia, COUNT(p) " +
            "FROM Post p WHERE p.usuario.id = :usuario_id " +
            "GROUP BY p.materia.id, p.materia.materia " +
            "ORDER BY p.materia.id")
    List<Object[]> findMateriaByUsuario(@Param("usuario_id") Long usuarioId);

    @Query("SELECT p FROM Post p WHERE p.usuario.id = :usuario_id AND p.vista = true ORDER BY p.fecha DESC")
    List<Post> findAllByUsuarioIdAndVistaTrue(@Param("usuario_id") Long usuario_id);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Post p WHERE p.id = :postId AND p.usuario.id = :usuarioId AND p.vista = :vista")
    boolean existsByIdAndUsuarioIdAndVista(@Param("postId") Long postId,
                                           @Param("usuarioId") Long usuarioId,
                                           @Param("vista") boolean vista);

}