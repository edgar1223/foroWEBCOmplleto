package com.foroweb.foroweb.repository;


import com.foroweb.foroweb.model.Alumno;
import com.foroweb.foroweb.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    @Query("FROM Materia")
    List<Materia> findAllMateria();

    @Query("SELECT m FROM Materia m WHERE m.materia IN :nombres")
    Set<Materia> findByNombres(@Param("nombres") Set<String> nombres);

}