package com.foroweb.foroweb.repository;


import com.foroweb.foroweb.dto.UsuarioDTO;
import com.foroweb.foroweb.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.foroweb.foroweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("FROM Alumno")
    List<Alumno> findAllAlumnos();

    @Query("FROM Profesor p WHERE p.departamento.id = :departamentoId")
    List<Profesor> findAllProfesores(@Param("departamentoId") Long departamentoId);

    Optional<Usuario> findByEmail(String email);



    @Query("FROM Usuario WHERE id = :id")
    Optional<Usuario> findById(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.nombre = :nombre, u.apellido = :apellido, u.email = :email, u.password = :password, u.img = :img WHERE u.id = :id")
    void updateUsuario(@Param("id") Long id,
                       @Param("nombre") String nombre
            ,@Param("apellido") String apellido,
                       @Param("email") String email,
                       @Param("password") String password,
                       @Param("img") String img);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.notificaciones = :notificaciones WHERE u.id = :id")
    void updateUsuarioNotificaciones(@Param("id") Long id,@Param("notificaciones") int notificaciones);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.nombre = :nombre, u.apellido = :apellido, u.email = :email, u.password = :password WHERE u.id = :id")
    void updateUsuarioImg(@Param("id") Long id,
                       @Param("nombre") String nombre
            ,@Param("apellido") String apellido,
                       @Param("email") String email,
                       @Param("password") String password);
}
